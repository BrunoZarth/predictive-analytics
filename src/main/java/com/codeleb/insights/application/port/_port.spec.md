# Application Ports Specification

## Responsibility
Defines the `Inbound` and `Outbound` contracts (interfaces) connecting the core application to the outside world.

## AI Instructions
- **Inbound (`port/in`)**: Interfaces defining the exact Use Cases exposed by the application (e.g., `ProcessTransactionUseCase`). Driven by Web/Controllers.
- **Outbound (`port/out`)**: Interfaces required by the application to access external state (e.g., databases, external APIs, event buses). E.g., `SaveTransactionPort`. Implemented by Persistence Adapters.
- **Rules**: Everything here is a pure public Java interface. NO IMPLEMENTATION LOGIC. NO SPRING ANNOTATIONS. Data traveling through these ports MUST be pure Domain Models or Application-specific Command/Result DTOs, NEVER JPA Entities or Web Request DTOs.
