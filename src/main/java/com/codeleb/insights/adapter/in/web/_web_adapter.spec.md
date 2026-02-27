# Web Inbound Adapter Specification

## Responsibility
Serves as the Delivery Mechanism (Driving Adapter). Translates HTTP/REST requests into Use Case commands, executes them via Inbound Ports, and translates the result back into HTTP responses.

## AI Instructions
1. **Controllers**: Annotate with `@RestController`, `@RequestMapping`.
2. **Injection**: Autowire only `com.codeleb.insights.application.port.in.*` interfaces via Constructor Injection. A Controller MUST NEVER talk to a Repository or Outbound Port directly.
3. **DTOs**: Ensure robust input validation (`@NotBlank`, `@Min`, etc.) on Web DTOs. Map Web DTOs to Domain/Application Models (preferably using MapStruct) before calling the Application Port.
4. **Exceptions**: Handle domain exceptions gracefully utilizing `@RestControllerAdvice` in `GlobalExceptionHandler` and translate them into RFC 7807 Problem Details or standard error JSON.
