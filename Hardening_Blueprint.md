# 🛡️ Phase 5 Blueprint: Hardening & Deployment (Step 0)

**Date:** 2026-02-27  
**Participants:** `@architect`, `@tech-lead-reviewer`, `@devops-engineer`, `@security-auditor`

---

## 🏗️ 1. Architectural Strategy: The `config` Boundary (`@architect`)

*The Problem:* The core Domain and Application layers are pure Java. They know nothing of `@Service`, `@Repository`, or `@Autowired`. How does Spring Boot compose the object graph?
*The Solution:* We must establish a strict `com.codeleb.insights.config` package.
*   **The Blueprint:** We will create standard Spring `@Configuration` classes acting as factories.
    *   `UseCaseConfig.java`: Will manually instantiate the Application Services (like `TransactionFacadeService`) using the constructor injection of the injected Adapter Beans.
    *   This satisfies both Spring's IoC container and the Hexagonal mandate: The Application layer remains untainted by framework imports.

## 🔐 2. Security & Edge Auditing (`@security-auditor`)

*The Problem:* As we expose the MVP, what are our immediate attack vectors at the boundaries?
*The Solution:*
*   **Input Boundary (Web):** We already implemented Jakarta `@Valid` on `TransactionRequest`, preventing Nulls and Malformed Data. We must ensure Jackson cleanly handles unexpected JSON fields to prevent Mass Assignment (`spring.jackson.deserialization.fail-on-unknown-properties=true`).
*   **Output Boundary (Web):** Ensure the `GlobalExceptionHandler` masks internal stack traces and only leaks the RFC 7807 problem details.
*   **Persistence Boundary:** Spring Data JPA's repository pattern inherently uses Prepared Statements via Hibernate, neutralizing raw SQL Injection risks.

## 🚀 3. Execution Environments & Profiles (`@devops-engineer`)

*The Problem:* We need a seamless transition for the TDD cycle (H2) and Production readiness (PostgreSQL).
*The Solution:*
*   **`application.yml` (Default Profile):** Tuned for local development and MVP showcasing.
    *   `spring.datasource.url=jdbc:h2:mem:predictive_db`
    *   `spring.jpa.hibernate.ddl-auto=update`
    *   Enable H2 Console (`spring.h2.console.enabled=true`) for visual debugging during the interview.
*   **`application-prod.yml` (Production Profile):**
    *   Rigid connection pooling (HikariCP) directed to PostgreSQL.
    *   Fail-fast on startup if connection is refused.
*   **API Documentation:** We will implement `springdoc-openapi-starter-webmvc-ui` to auto-generate the Swagger UI from our Spring Web annotations.

## 🐳 4. Dockerization Strategy (`@devops-engineer` & `@tech-lead-reviewer`)

*The Problem:* Deploying a raw JAR is brittle. We need repeatable, OS-agnostic execution.
*The Solution:* A Multi-Stage Dockerfile based on Eclipse Temurin 17.
*   **Stage 1 (Build):** Uses Maven image to resolve dependencies and `mvn clean package -DskipTests` (tests are run earlier in the CI pipeline).
*   **Stage 2 (Runtime):** Uses a lean JRE Alpine image.
*   **Security Shift-Left:** The container will NOT run as `root`. We will create a dedicated `spring` user group within the Dockerfile to run the process.

---

### 🛑 Potential Blind Spots Identified
*   *Missing Open-API Annotations:* We need to ensure the `@api-designer` heavily annotates the Controller for the Swagger generation to be interview-ready.
*   *CORS Configuration:* By default, the API will reject cross-origin requests. Since this is a microservice likely consumed by an SPA, we need a global CORS configuration in the `config` layer.

### ✅ VERDICT
The Hardening & Deployment strategy is mathematically sound and respects the Hexagonal isolations. Proceed to Step 1: Specification & Implementation.
