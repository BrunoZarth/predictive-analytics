# Domain Exceptions Specification

## Responsibility
Contains all custom RuntimeExceptions that represent business rule violations within the domain.

## AI Instructions
1. When a business rule fails inside a `domain/model` or `domain/service`, throw a strictly contextual exception from this package.
2. NEVER use standard Java generic exceptions (`IllegalArgumentException`) for core domain concepts. Always create a semantic exception (e.g., `InvalidFinancialAmountException`).
3. Domain exceptions DO NOT know about HTTP Status Codes (e.g., `@ResponseStatus`). Leave HTTP translation to the `GlobalExceptionHandler` in the Web Adapter layer.
