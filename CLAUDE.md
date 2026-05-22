# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Language Preferences

- Always respond in Simplified Chinese.
- Git commit messages must be written in Chinese.

## Project Overview

Forma is a form/questionnaire building platform backend built on Spring Boot 3.5 + Java 21 + PostgreSQL. Core features
include drag-and-drop form design, skip logic via rule definitions, JSONB flexible storage, and event-driven
architecture.

## Build, Test, and Run

```bash
# Run application (requires PostgreSQL at 127.0.0.1:5432/forma)
./mvnw spring-boot:run

# Build project
./mvnw clean package

# Run all tests
./mvnw test

# Run single test
./mvnw test -Dtest=FormCommandServiceTest

# Flyway migrations run automatically on startup
# New migrations: src/main/resources/db/migration/V{version}__description.sql
```

- **Application config**: `src/main/resources/application.yaml`
- **Database**: PostgreSQL at `jdbc:postgresql://127.0.0.1:5432/forma`, user `forma`, password `pwd_forma`
- **JPA ddl-auto**: `validate` — schema is managed by Flyway only, never by Hibernate

## Architecture Overview

### DDD Core Domain Boundaries

The system is divided into the following core business domains based on DDD. Currently organized by packages, with plans
to split into separate modules by domain. Dependency rules must be strictly followed.

- **forma-shared (Shared Domain):** Provides cross-domain shared kernel and common capabilities.
- **forma-iam (Identity & Access Control Domain)**: Handles user authentication, authorization, and permission
  management.
- **forma-form (Form Design & Management Domain)**: Manages form structure definition, form collaboration, and version
  management.
- **forma-submission (Data Submission & Storage Domain)**: Handles form instance data submission, draft saving,
  validation, and querying.
- **forma-analysis (Analysis Domain)** [Planned]: Manages form data aggregation, report generation, and visualization
  dashboards.

**Dependency Rules**: Business domains can only have one-way dependencies. Circular dependencies are strictly
prohibited.Dependency direction: submission → form → shared, iam → shared. Any domain may depend on shared, but shared
cannot depend on any business domain.

### CQRS Read/Write Separation

- **Command (Write)**: Uses ORM (JPA/Hibernate) for domain model persistence, ensuring data consistency.

- **Query (Read)**: Uses JDBC Template (or native SQL) for direct database queries, returning read-optimized DTOs/view
  objects, bypassing the domain model.

- **Principle**: Command and Query share the same database but are logically separated. Write operations go through the
  domain model; read operations use direct SQL queries.

All converters are in `shared.config.JpaConverters` with `@Converter(autoApply = true)`.

### Value Objects with JPA Converters

- `FormId` (UUID) — `@Embeddable`, used as `@EmbeddedId` on Form
- `FormSubmissionId` (FormId, submissionNo) — `@Embeddable`, used as `@EmbeddedId` on FormSubmission
- `OperatorId` (String) — @Embeddable, current operator for auditing (comes from UserId in iam domain)
- `FieldCode` (String) — standalone record, used as Map key in submission content, unique within a form
- `FormRoleCode` (String) — form role code, unique within a form

### JSONB Polymorphic Serialization

`FieldDefinition` uses Jackson `@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")` for polymorphic
deserialization of form fields stored in `forms.fields` (JSONB). The `@JsonSubTypes` mapping maps type names to concrete
classes — some types share implementations (e.g., text/textarea/email/phone all map to `TextField`;
radio/checkbox/select map to `SelectField`).

`FormSubmission.content` is `Map<FieldCode, Object>` stored as JSONB — each key is a field code, value is the submitted
data.

### Event System

Events use a typed envelope pattern:

- `DomainEvent` — interface with `eventType()` and `eventVersion()` (e.g., `FormCreatedV1`)
- `FormaEvent<T extends DomainEvent>` — envelope with `eventId` (UUID v7 via `GUID.v7()`), `eventAt`, `eventType`,
  `eventVersion`, `traceId`, `payload`
- `EventRegistry` — maps `"eventType:vN"` strings to concrete DomainEvent classes for deserialization
- `Events` — static serialize/deserialize utility using Jackson

Current registered events: `form.created:v1`, `form.published:v1`, `submission.submitted:v1`

### Soft Delete Pattern

Entities use Hibernate annotations for logical deletion:

```java
@SQLDelete(sql = "UPDATE forms SET deleted_at = now() WHERE form_id = ?")
@SQLRestriction("deleted_at is null")
```

Applied to all entities extending `BaseEntity`. All queries automatically exclude soft-deleted records.

### Form Roles and Collaborators

- `FormRole` — per-form role with `OperationPermission` enum (submission/form CRUD, approve, assign) and
  `AccessPermission` (field-level + record filter conditions)
- `FormCollaborator` — links a UserId to a FormId with a FormRoleCode; partial unique index on
  `(form_id, operator_id) WHERE deleted_at IS null`
- `FormFactory` creates default roles (owner, admin, viewer) and the owner collaborator on form creation

### Error Handling

- `ErrorCode` interface — numeric codes by module: 1xxx general, 2xxx user, 3xxx form, 9xxx system
- Exception hierarchy: `BusinessException` (with code + detail), `ResourceNotFoundException`,
  `PermissionDeniedException`, `OperationNotAllowException`, `SystemException`
- `GlobalExceptionHandler` returns RFC 7807 `ProblemDetail` responses with `code` and `timestamp` properties
- Success responses use `Result<T>` wrapper: `{ "code": 200, "message": "", "data": T }`

### Database Structure

#### Migration Management

Database is version-managed via Flyway. All changes must be implemented through migration scripts.

- **Migration file location**：`src/main/resources/db/migration/`
- **Naming convention**：`V{版本号}__{描述}.sql`（e.g., `V0.1.0__init_table.sql`）
- **Execution timing**：Automatically executed in version order on application startup

**Note**：Table structure evolves with new migration scripts. Always refer to the actual files in db/migration directory.
Already executed migration scripts must not be modified.

#### Core Tables (as of latest migration)

- `forms` — form definitions with JSONB fields/rules/settings, status (DRAFT/PUBLISHED/CLOSED)
- `form_versions` — published snapshots with `form_content` JSONB
- `form_submissions` — submission data with `content` JSONB map, `form_id` + `form_version` reference
- `form_roles` — per-form roles with JSONB operation/access permissions
- `form_collaborators` — user-to-form role assignments

All tables have `created_by`, `created_at`, `updated_by`, `updated_at`, `updated_ip` (inet), `deleted_at` audit columns
via `BaseEntity`.

## Code Conventions

- All database operations must go through Repository layer. Writing SQL directly in Service layer is prohibited.
- Prefer `@RequiredArgsConstructor` + final fields for dependency injection in Spring beans, or use `@Setter(onMethod_ =
  @Autowired)`
- API endpoints are under `/v1/` prefix with springdoc-openapi annotations (`@Tag`, `@Operation`)
- `BaseEntity` provides JPA auditing (`@CreatedDate`, `@CreatedBy`, `@LastModifiedDate`, `@LastModifiedBy`) and soft
  delete (`deletedAt`)
- Spring Security dependency is present but not yet configured
