# Adapter Layer Specification

## Responsibility
The `adapter` layer connects the core application to external interfaces and infrastructure. It handles HTTP REST APIs (Web Inbound Adapters) and databases (Persistence Outbound Adapters).

## Strict Import Boundaries
- **ALLOWED**: `com.codeleb.insights.application.*`, `com.codeleb.insights.domain.*`, Spring Web, Spring Data JPA, MapStruct, Lombok.
- **FORBIDDEN**: Direct implementation of core business logic. Adapters must only convert data and delegate to Application Ports.

## Operating Agents
- `@api-designer`
- `@java-backend`
