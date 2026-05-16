# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 语言偏好

请始终使用简体中文回复。

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

### CQRS Package Structure

The `form` domain follows Command Query Responsibility Segregation:

```
wander.nights.forma.form
├── command/                    # Write side
│   ├── entity/                 # JPA entities (Form, FormVersion, FormSubmission, FormRole, FormCollaborator)
│   ├── repository/             # Spring Data JPA repositories
│   ├── service/                # Command service interfaces + impl/
│   ├── dto/                    # Command DTOs (FormCreateCommand, SubmissionSubmitCommand, etc.)
│   └── service/FormFactory     # Entity construction factory (IDs, defaults, roles)
├── query/                      # Read side
│   ├── service/                # Query service interfaces + impl/
│   └── dto/                    # Read DTOs / VOs
└── controller/                 # REST controllers (inject both command and query services)
```

Command and query services are separate interfaces with separate implementations. Controllers inject both sides.

### Value Objects with JPA Converters

Domain identifiers and typed codes are modeled as Java records with `@Embeddable` or standalone, converted via
`JpaConverters`:

- `FormId` (UUID) — `@Embeddable`, used as `@EmbeddedId` on Form
- `FormSubmissionId` (UUID) — `@Embeddable`, used as `@EmbeddedId` on FormSubmission
- `UserId` (String) — `@Embeddable`, auto-applied converter
- `FieldCode` (String) — standalone record, used as Map keys in submission content
- `FormRoleCode` (String) — auto-applied converter

All converters are in `shared.config.JpaConverters` with `@Converter(autoApply = true)`.

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

Applied to: Form, FormSubmission. All queries automatically exclude soft-deleted records.

### Form Roles and Collaborators

- `FormRole` — per-form role with `OperationPermission` enum (submission/form CRUD, approve, assign) and
  `AccessPermission` (field-level + record filter conditions)
- `FormCollaborator` — links a UserId to a FormId with a FormRoleCode; partial unique index on
  `(form_id, user_id) WHERE deleted_at IS null`
- `FormFactory` creates default roles (owner, admin, viewer) and the owner collaborator on form creation

### Error Handling

- `ErrorCode` interface — numeric codes by module: 1xxx general, 2xxx user, 3xxx form, 9xxx system
- Exception hierarchy: `BusinessException` (with code + detail), `ResourceNotFoundException`,
  `PermissionDeniedException`, `OperationNotAllowException`, `SystemException`
- `GlobalExceptionHandler` returns RFC 7807 `ProblemDetail` responses with `code` and `timestamp` properties
- Success responses use `Result<T>` wrapper: `{ "code": 200, "message": "", "data": T }`

### Database Schema

Five tables managed by Flyway (`V0.1.0__init_table.sql`):

- `forms` — form definitions with JSONB fields/rules/settings, status (DRAFT/PUBLISHED/CLOSED)
- `form_versions` — published snapshots with `form_content` JSONB
- `form_submissions` — submission data with `content` JSONB map, `form_id` + `form_version` reference
- `form_roles` — per-form roles with JSONB operation/access permissions
- `form_collaborators` — user-to-form role assignments

All tables have `created_by`, `created_at`, `updated_by`, `updated_at`, `updated_ip` (inet), `deleted_at` audit columns
via `BaseEntity`.

## Key Conventions

- Controllers use `@Setter(onMethod_ = @Autowired)` for DI (Lombok), services use `@RequiredArgsConstructor`
- API endpoints are under `/v1/` prefix with springdoc-openapi annotations (`@Tag`, `@Operation`)
- `FormFactory` centralizes entity construction — IDs are generated with `GUID.v7().toUUID()` (uuid-creator library)
- `BaseEntity` provides JPA auditing (`@CreatedDate`, `@CreatedBy`, `@LastModifiedDate`, `@LastModifiedBy`) and soft
  delete (`deletedAt`)
- IP auditing in `BaseEntity` (`@PreUpdate`/`@PrePersist`) is commented out pending `AuditContext` implementation
- Spring Security dependency is present but not yet configured
