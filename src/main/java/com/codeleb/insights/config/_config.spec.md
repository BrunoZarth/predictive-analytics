# Configuration Layer Specification

## Responsibility
The `config` layer wires the application together. It acts as the Composer, assembling the implementations from the `adapter` layer with the services from the `application` layer via Spring's dependency injection mechanisms.

## Strict Import Boundaries
- **ALLOWED**: Any package within `com.codeleb.insights.*`, `org.springframework.context.annotation.*`, etc.
- **RULE**: This is the only place where manual bean wiring and major layout orchestration should happen.

## Operating Agents
- `@architect`
- `@devops-engineer`
