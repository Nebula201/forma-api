# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Forma is a form/ questionnaire building platform backend service built on Spring Boot 3.5 + Java 21 + PostgreSQL. Core features include drag-and-drop form design, complex skip logic, JSONB flexible storage, and event-driven architecture for business decoupling.

## Build, Test, and Run

### Development Commands

```bash
# Run application
./mvnw spring-boot:run

# Build project
./mvnw clean package

# Run tests
./mvnw test

# Run single test
./mvnw test -Dtest=ShortIdTest

# Database migrations
# Flyway migrations are automatically executed on startup
# New migrations: src/main/resources/db/migration/V{version}__description.sql
```

### Configuration

- **Application config**: `src/main/resources/application.yaml`
- **Database**: PostgreSQL at `jdbc:postgresql://127.0.0.1:5432/forma`
- **Flyway baseline**: Enabled on first migration

## Architecture Overview

### Dual Persistence Strategy (JPA + JdbcTemplate)

- **JPA (80% use case)**: Standard CRUD, entity mapping, simple associations
- **JdbcTemplate (20% use case)**: Complex queries, batch operations, dynamic DDL, JSONB manipulation

**Important**: Clear cache after JdbcTemplate updates with `jdbcTemplate.update()`.

### Event-Driven Design

All key business operations emit events (create, publish, submit, delete, update):
- Uses Spring's `ApplicationEventPublisher` for built-in event bus
- All listeners use `@EventListener` + `@Async` for async processing
- Event data is self-contained (no reverse DB queries in listeners)
- Failed event processing doesn't rollback main transaction
- Consider RabbitMQ for reliable cross-service delivery (future)

### Form Versioning Strategy

**Core concept**: Different versions create different formSubmission tables, but minor versions (is_minor=true) reuse existing tables.

```
form_versions table:
- form_content: JSONB snapshot of form definition
- is_minor: TRUE = only text/rule changes, no new table
- submission_table: Name of formSubmission table (e.g., submissions_{shortId}_v{version})

Migration path:
1. Version 1 → Field changes: Create submissions_form_xxx_v1
2. Version 2 (minor, is_minor=true) → No new table, data remains in v1
3. Version 3 (major, is_minor=false) → Create v3, new submissions go to v3, v1 kept for history
```

### Submission Data Management

Each form version has its own formSubmission table with:
- **Fixed columns**: id, submission_id (UUID v7), respondent_id, submitted_at, ip_address, duration_seconds, status, created_at, updated_at
- **Dynamic columns**: Created from `form_content.fields` definition
- **Indexes**: Auto-created for key fields (submission_id, submitted_at, status, filterable fields like radio/select/rating)

### Logical Deletion Pattern

Forms use `@SQLDelete` and `@SQLRestriction` instead of actual DELETE:
```java
@SQLDelete(sql = "UPDATE forms SET deleted_at = now() WHERE form_id = ?")
@SQLRestriction("deleted_at is null")
```

All queries automatically filter out soft-deleted records.

### JSONB Field Storage

Fields are stored in JSONB arrays in `forms.fields` and `forms.rules`:
- Uses polymorphic `FieldDefinition` hierarchy with Jackson `@JsonTypeInfo` for type differentiation
- Field types: text, textarea, email, phone, radio, checkbox, select, rating, date, time, datetime, number, file
- Checkbox/select fields store multiple values as JSONB arrays
- FieldDefinition hierarchy: `TextField`, `SelectField`, `RatingField`, `DateField`, `FileField`

## Key Patterns and Conventions

### Entity Design

- All entities in `wander.nights.forma.entity`
- Use Lombok for boilerplate (`@Data`, `@Setter(onMethod_ = @Autowired)`)
- JPA audit fields: `@CreatedDate`, `@CreatedBy`, `@LastModifiedDate`, `@LastModifiedBy`
- Logical deletion with `@SQLDelete` and `@SQLRestriction`

### Service Layer

- Business logic in `wander.nights.forma.service`
- JdbcTemplate for dynamic table operations (DDL, complex queries)
- Keep JPA and JdbcTemplate usage clearly separated
- Validation service (SubmissionValidationService) is implemented but commented out
- Current status: Core validation logic exists, needs to be integrated with formSubmission flow

### Controller Layer

- RESTful endpoints under `/v1/` prefix
- Use `Result<T>` wrapper for responses: `{ "code": 200, "message": "success", "data": {...} }`
- Validation via Jakarta `@Valid`

### Event System

- Base class: `FormaEvent` (abstract) - includes eventId, eventType, eventAt, traceId
- Uses `GUID.v7().toUUID()` for generating UUID v7 in events
- Event listeners should be lightweight and self-contained
- Avoid reverse database queries in event handlers

### Database Migrations

- Flyway format: `V{version}__description.sql`
- Location: `src/main/resources/db/migration/`
- Each migration script must be idempotent (use `CREATE TABLE IF NOT EXISTS`, etc.)

### Testing

- Main entry point: `FormaApplicationTests.java`
- Test structure mirrors main code
- Mock dependency injection with Lombok `@Setter(onMethod_ = @Autowired)` in tests
- Existing test: `ShortIdTest.java` - Base62 short code generation for form short_id field

## Important Design Decisions

1. **JPA + JdbcTemplate hybrid**: Keep distinct - JPA for CRUD, JdbcTemplate for complex operations. Clear cache after JdbcTemplate updates.

2. **Event data self-containment**: Don't query DB from listeners. Pass necessary data in events.

3. **API versioning**: All APIs use `/v1/` prefix from the start.

4. **Logical deletion**: Forms use soft delete, not hard delete. Query automatically filters out deleted records.

5. **Version isolation**: Different form versions create different formSubmission tables. Minor versions reuse existing tables.

6. **Skip logic validation**: The validation service is implemented but commented out. It validates:
   - Visible fields based on skip rules
   - Hidden fields don't have values
   - Required field validation
   - Type-specific validation (email, phone, rating, checkbox)

## Current Implementation Status

**Implemented**:
- Database schema and Flyway migrations
- Entity models (Form, FormVersion)
- Basic field types (TextField, SelectField, RatingField, DateField, FileField)
- Form formSubmission table creation with dynamic DDL
- Submission validation logic (commented out, ready to integrate)
- Event base class structure

**To Complete**:
- Form repository and CRUD operations
- Form versioning and publishing logic
- Submission flow integration (Controller ↔ Service ↔ Repository)
- Skip logic engine implementation
- Event listener implementations
- Controller implementations for full API
- Database query optimization for formSubmission retrieval
- Statistics and reporting features
