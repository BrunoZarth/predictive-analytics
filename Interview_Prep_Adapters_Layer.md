# 🎓 Interview Preparation Guide: The Adapters Layer (Driving & Driven Edges)

**Role:** Senior/Staff Software Engineer  
**Focus:** Hexagonal Architecture (Ports & Adapters), Spring Web MVc, Spring Data JPA, Impedance Mismatch, Context Slicing.

---

## 1. The "Elevator Pitch" for the Adapters Layer
**The Question:** *"Explain the role of the Adapters layer in your Hexagonal (Ports & Adapters) Architecture. Why not just let your Controllers call your Repositories directly?"*

**Your Pitch:**
> *"In a true Hexagonal Architecture, the Adapters form the outermost shell of the system. Their sole responsibility is I/O—translating signals between the external world and the pure Core (Domain/Application). We split them cleanly into **Driving Adapters** (like REST Controllers) that trigger use cases, and **Driven Adapters** (like JPA Repositories) that the application uses to access infrastructure.*
> 
> *If a Controller calls a Repository directly (the traditional Layered pattern), we couple our HTTP endpoints tightly to our database schema. In our Predictive Analytics service, an Adapter is just a 'dumb' translator. The `TransactionController` translates JSON into Domain commands, and the `TransactionJpaAdapter` translates Domain commands into SQL. This absolute decoupling means I can swap PostgreSQL for DynamoDB, or REST for gRPC, without ever touching a single line of business logic."*

---

## 2. Deep Dive: Driving Adapters (The Web Layer)

### The "Dumb Controller" Paradigm
**The Question:** *"Controllers often become bloated. How do you keep them clean in enterprise systems?"*

**The Answer (The "Why"):**
*"A Controller's job is purely HTTP negotiation: routing, extracting parameters/headers, and determining HTTP status codes. In our project, `TransactionController` literally does nothing but take a `@Valid TransactionRequest`, enforce the `Idempotency-Key` at the edge, map it to a Domain `Transaction`, and push it to the `ProcessTransactionUseCase`. No ifs, no loops. If a controller has a business conditional (like `if amount < 0`), it's a code smell and a boundary violation."*

### RFC 7807 (Problem Details for HTTP APIs)
**The Question:** *"How do you handle exceptions in a distributed system?"*

**The Answer (The "Why"):**
*"I use `GlobalExceptionHandler` with `@RestControllerAdvice` to map internal exceptions to formally standardized RFC 7807 **Problem Details**. When the rich Domain throws a `BusinessRuleViolationException` deep inside the core, or when Jakarta validation fails on the DTO, the advice translates this into a standard JSON payload with `type`, `title`, `status`, and `detail`. This provides consistent, predictable contracts for SPA frontends and other microservices, a hard requirement in places like SAP."*

---

## 3. Deep Dive: Driven Adapters (The Persistence Layer)

### The Impedance Mismatch (Domain vs. SQL)
**The Question:** *"Why do you have a `Transaction` domain object and a separate `TransactionJpaEntity`? Isn't that redundant boilerplate?"*

**The Answer (The "Why"):**
*"It is the most critical architectural decision in the project. The Object-Relational Impedance Mismatch dictates that relational tables and rich Java objects model data differently. If I put JPA annotations (`@Entity`, `@Table`) on my Domain `Transaction` record, I am polluting my pure business logic with database constraints.* 

*In our codebase, the Domain `Transaction` is a rich `record` focusing on behavior. The `TransactionJpaEntity` is a completely anemic Java Bean with an empty constructor and setters, existing solely to satisfy Hibernate's proxy requirements. We bridge them exactly at the boundary using MapStruct (`TransactionMapper`), keeping the Domain 100% ignorant of the database."*

### Context Slicing & Docker Flakiness
**The Question:** *"Walk me through your testing strategy for Repositories."*

**The Answer (The "Why"):**
*"Integration testing the database requires strict context slicing. We don't spin up the whole application; we use `@DataJpaTest` to load only Hibernate and Spring Data. Originally, we targeted a real `PostgreSQLContainer` via Testcontainers to ensure SQL dialact accuracy. However, in deeply nested virtualization environments (like Docker-in-WSL), image locales or boot times can flake (`ContainerLaunchException`). For local TDD agility, we can quickly swap to an H2 in-memory fallback since JPA abstracts the dialect, guaranteeing that our developers are never blocked by local infrastructure quirks, while CI/CD pipelines run the heavy Testcontainers."*

---

## 4. Top 3 "Gotcha" Interview Questions

### 🧨 Gotcha 1: The `@Autowired` Trap
**Recruiter:** *"I see you use Constructor Injection. Why not just use `@Autowired` on the fields in your Adapters? It saves so much typing."*
**You:** *"Field injection masks dependencies, making it easy to create 'God Classes', and severely complicates unit testing because you need reflection or Spring containers to inject mocks. Constructor injection guarantees the object is always fully instantiated in a valid state (fail-fast), allows fields to be `final` for immutability, and makes pure POJO unit testing trivial."*

### 🧨 Gotcha 2: MapStruct vs. ModelMapper
**Recruiter:** *"Why MapStruct? Why not ModelMapper, which uses Reflection and requires zero boilerplate code?"*
**You:** *"Performance and Type Safety. ModelMapper uses runtime reflection, which is slow and prone to breaking unexpectedly in production if field names change. MapStruct generates pure Java code at compile-time. If there is a mapping mismatch (like `id` vs `transactionId`), MapStruct fails the build immediately (which actually happened and we fixed explicitly with `@Mapping`). Fail fast at compile time is vastly superior to failing at runtime."*

### 🧨 Gotcha 3: Pagination at the Edge
**Recruiter:** *"Why use `Pageable` in the Adapter instead of fetching lists and mapping them?"*
**You:** *"Memory constraint. The `loadTransactionPort` utilizes Spring Data's `PagingAndSortingRepository`. If a tenant has 10 million transactions, fetching a `List` causes a catastrophic `OutOfMemoryError`. Pushing `Pageable` all the way down to the ORM ensures `LIMIT` and `OFFSET` are executed at the database engine level, retrieving only the exact subset needed."*
