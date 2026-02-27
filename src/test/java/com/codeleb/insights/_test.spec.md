# Testing Layer Specification (`src/test`)

## Responsibility
The `test` directory is not just for verification; in SpecDD/TDD, it is the executable specification of the project. It defines the behavior, boundaries, and architecture before the production code is written.

## Strict Rules for AI Generation
- **Architecture Tests (`architecture`)**: Use ArchUnit. These tests must validate that Hexagonal Architecture dependency rules (Ports and Adapters) are strictly adhered to.
- **Domain Tests (`domain`)**: Pure Java. NO SPYING OR MOCKING FRAMEWORKS allowed unless dealing with highly complex value objects. Instantiate records and strategies manually. Assert domain rules and exceptions.
- **Application Tests (`application`)**: Mock the `Outbound Ports` (e.g., `LoadTransactionPort`) using Mockito. Verify that use cases orchestrate the domain logic properly and call the outbound ports with the correct transformed models.
- **Adapter Tests (`adapter`)**: 
  - *Web*: Use `@WebMvcTest`. Mock the `Inbound Ports` (Use Cases). Test HTTP status codes (200, 400, 500), JSON serialization, and `@Valid` logic.
  - *Persistence*: Use `@DataJpaTest` and Testcontainers. Real database tests mapping Domain Models <-> Entities via MapStruct.

## Operating Agents
- `@qa-tester`
- `@java-backend`
