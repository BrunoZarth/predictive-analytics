# Persistence Outbound Adapter Specification

## Responsibility
Implements the Driven Adapters. Interacts with the database via Spring Data JPA to fulfill the contracts defined by the Application Outbound Ports.

## AI Instructions
1. **Adapters**: Classes annotated with `@Component` or `@Repository` that explicitly `implements` interfaces from `com.codeleb.insights.application.port.out`.
2. **Entities**: Define standard JPA `@Entity` classes here. These entities MUST NOT leak out of the `adapter/out/persistence` package.
3. **Repositories**: Extend `JpaRepository`.
4. **Mapping**: The Persistence Adapter class is responsible for receiving a `Domain Model` from the Application Port, mapping it to a `JPA Entity`, persisting it via the `JpaRepository`, and returning mapped `Domain Models`. Do this utilizing MapStruct interfaces kept locally in this package.
