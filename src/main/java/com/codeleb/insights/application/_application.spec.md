# Application Layer Specification (Phase 3)

**Architect:** `@architect`
**Reviewer:** `@tech-lead-reviewer`

## 1. Enterprise Mandates (Tech Lead Directives)
Before implementing this layer, the `@java-backend` agent MUST adhere to the following non-functional requirements:
1.  **Idempotency:** The Use Case that receives transactions must handle duplicates gracefully. We will require an HTTP `Idempotency-Key` or rely on the `Transaction.id` for UPSERT/Ignore logic in the application service.
2.  **Performance (Pagination/Chunking):** When processing predictive insights for a customer, the application CANNOT load millions of history rows into RAM. The Outbound Port for fetching transactions MUST be paginated (`Pageable` or chunked streams).
3.  **Observability (MDC):** Every Use Case execution must inject the `customerId` and a `correlationId` into the logging context (SLF4J MDC) so that SREs can trace insight generation across microservices.

## 2. Inbound Ports (Use Cases)
These are interfaces that the Web Adapter will call.

*   `ReceiveTransactionUseCase.java`
    *   **Method:** `void processTransaction(Transaction transaction);`
    *   **Constraint:** Must enforce idempotency.
*   `GenerateCustomerInsightsUseCase.java`
    *   **Method:** `List<ActionableInsight> generateForCustomer(String customerId);`
    *   **Constraint:** Must use paginated reads from the Outbound Port to feed the `PredictionStrategy`.

## 3. Outbound Ports (Driven Adapters)
These are interfaces that the Use Cases will call to reach the outside world (Database, Messaging).

*   `TransactionRepositoryPort.java`
    *   **Methods:** 
        *   `void save(Transaction transaction);` (Idempotent save)
        *   `Page<Transaction> findByCustomerId(String customerId, Pageable pageable);` (Memory safe retrieval)
*   `InsightPublisherPort.java`
    *   **Method:** `void publish(ActionableInsight insight);` (e.g., to a Kafka topic or WebSocket).
