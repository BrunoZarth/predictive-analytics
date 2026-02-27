# Adapter Layer Specification (Phase 4)

**Architect:** `@architect`
**Reviewer:** `@tech-lead-reviewer`

## 1. Enterprise Mandates (Tech Lead Directives)
Before implementing this layer, the `@api-designer` and `@java-backend` agents MUST adhere to the following:
1.  **Data Isolation & Multi-tenancy:** The REST Controllers MUST extract the `customerId` from a secure context (e.g., JWT Token from the `Authorization` header), NEVER trusting a `customerId` passed in the raw JSON payload for reads.
2.  **Idempotency Header:** The POST endpoint for receiving transactions MUST accept a custom HTTP header `Idempotency-Key`.
3.  **Error Handling (RFC 7807):** All Domain Exceptions (`BusinessRuleViolationException`) must be translated into standardized JSON Problem Details (HTTP 400 or 422) by a global `@RestControllerAdvice`.

## 2. Inbound Adapters (Driving - Web)
*   `TransactionController.java`
    *   **Responsibilities:** Receives POST requests, validates format via `@Valid` DTOs, extracts Idempotency Key, maps to Domain `Transaction`, and invokes `ReceiveTransactionUseCase`.
*   `InsightController.java`
    *   **Responsibilities:** Receives GET requests, extracts `customerId` from JWT context, invokes `GenerateCustomerInsightsUseCase`, and returns a standard JSON response.

## 3. Outbound Adapters (Driven - Persistence/Messaging)
*   `TransactionJpaAdapter.java`
    *   **Responsibilities:** Implements `TransactionRepositoryPort`. Uses Spring Data JPA repositories with `@Entity` mappings. Must implement the pagination (`Pageable`) contract safely.
*   `InsightKafkaAdapter.java` (Optional/Future)
    *   **Responsibilities:** Implements `InsightPublisherPort` to broadcast insights to event streams.
