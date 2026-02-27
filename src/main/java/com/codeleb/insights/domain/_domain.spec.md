# Domain Layer Specification

## Responsibility
The `domain` layer is the core of the Predictive Analytics microservice. It encapsulates pure, state-agnostic business logic, domain models, and essential business rules (e.g., calculation of moving averages).

## Strict Import Boundaries
- **ALLOWED**: Java Standard Library (`java.*`).
- **FORBIDDEN**: Any framework-specific imports (e.g., `org.springframework.*`, `javax.persistence.*`, `lombok.*` unless strictly for boilerplate reduction without tying to framework concepts).
- **FORBIDDEN**: Any references to `application`, `adapter`, or `config` packages. The Dependency Rule dictates that Domain depends on NOTHING.

## Operating Agents
- `@architect`
- `@data-domain`
