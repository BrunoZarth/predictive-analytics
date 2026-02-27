# Application Layer Specification

## Responsibility
The `application` layer oversees the orchestration of business use cases. It defines the inbound and outbound ports (interfaces) that bridge the domain with the outside world, and implements the services that execute domain logic using these ports.

## Strict Import Boundaries
- **ALLOWED**: `com.codeleb.insights.domain.*`, Java Standard Library.
- **FORBIDDEN**: Any technology-specific dependencies (e.g., `org.springframework.web.*`, SQL/JPA imports).
- **FORBIDDEN**: Any references to the `adapter` or `config` packages.

## Operating Agents
- `@architect`
- `@java-backend`
