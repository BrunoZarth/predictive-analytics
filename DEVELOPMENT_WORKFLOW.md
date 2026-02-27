# ⚙️ AI Orchestration & Development Workflow (v2.0 - Shift-Left Edition)

This document outlines the strict Spec-Driven Development (SpecDD) and Test-Driven Development (TDD) workflow for the **Predictive Analytics** project. As the Lead Orchestrator, you must follow this exact sequence to ensure architectural integrity, deep business logic, extreme code quality, and zero AI hallucination.

## 👥 The Agent Roster
Know your team. Only invoke an agent for their specific domain:
* `@architect`: System design, Hexagonal boundaries, and writing `.spec.md` contracts.
* `@tech-lead-reviewer`: **(Dual Role)** Audits initial business logic depth (Step 0) and audits final code quality (Step 4).
* `@qa-tester`: Writing failing tests (RED phase) based strictly on specs and deep business rules.
* `@data-domain`: Writing pure Java business/predictive logic (Domain layer).
* `@java-backend`: Implementing Use Cases and Persistence Adapters (Application & Adapter/Out layers).
* `@api-designer`: Implementing REST Controllers and Swagger (Adapter/In layer).
* `@security-auditor`: Checking for vulnerabilities (Injection, Data Leaks).
* `@devops-engineer`: Docker, CI/CD, and Spring Profiles.
* `@professor-mentor`: Explaining the "Why" for technical interview preparation (e.g., SAP).

---

## 🔄 The Master TDD Loop (The 6-Step Micro-Flow)
For every single feature or component, strictly follow this 6-step loop. **Never skip Step 0.**

### Step 0: DESIGN SYNC & DEEP THINKING (The Confrontation)
*Shift-Left Validation:* Before writing any spec or code, we must ensure the business model is not shallow, naive, or anemic. We cross-reference the goal with enterprise (SAP-level) standards.
* **Prompt:** *"@architect and @tech-lead-reviewer, we need to design [Feature Name]. Before generating specs, analyze this requirement for an enterprise environment. What are the blind spots? Are we missing critical entity fields (like currency, tenantId)? Are the business rules too simple? Debate and provide a deep, enterprise-grade data model and logic proposal."*

### Step 1: SPECIFY (The Contract)
Based on the conclusions of Step 0, define the strict architectural boundaries.
* **Prompt:** *"@architect based on the Step 0 debate, update the `_layer.spec.md` and Java stubs for this context. Define the rich interfaces/ports."*

### Step 2: RED (The Test)
Never write implementation code without a failing test that covers the deep business rules.
* **Prompt:** *"@qa-tester based on the `[Layer].spec.md` and the rich models, write the tests for [Class/Component]. Focus on boundary conditions, validations, and the complex business rules we defined."*
* *Action:* Run the test. Verify it fails (Compilation error or Assertion failure).

### Step 3: GREEN (The Implementation)
Call the specialist for that specific layer to make the test pass elegantly.
* **Prompt (Domain):** *"@data-domain write the pure Java implementation for [Class] to make the `@qa-tester` tests pass."*
* **Prompt (App/Adapter):** *"@java-backend / @api-designer write the implementation for [Class] to make the tests pass. Strictly follow the interface contract."*
* *Action:* Run the test. Verify it passes 100%.

### Step 4: REFACTOR & REVIEW (The Audit)
Do not merge or move on without a strict code review.
* **Prompt:** *"@tech-lead-reviewer review the implementation of [Class]. Check for Hexagonal architecture violations, code smells, and SOLID compliance. Is the blind spot detection clear? Approve or request changes."*
* *Action:* If changes are requested (`🛑 CHANGES REQUESTED`), apply them and ensure tests still pass. Proceed only on `✅ APPROVED`.

### Step 5: MENTORSHIP (The Interview Prep)
Consolidate your knowledge for technical interviews.
* **Prompt:** *"@professor-mentor explain the design patterns and architectural decisions we just used in [Component]. Give me an elevator pitch for a Senior Engineering interview at SAP."*

---

## 🏗️ Macro-Phases of Development (Inside-Out)
Because we are using Hexagonal Architecture, we build from the inside out. Do not jump layers.

### Phase 1: Architecture Skeleton (Done ✅)
* Generate the folder structure, `_layer.spec.md` files, and empty stubs.

### Phase 2: The Core (Domain Layer) (Done ✅)
* **Focus:** Pure business rules, predictive algorithms (Strategy pattern), rich domain models.
* **Rule:** ZERO Spring or external dependencies.

### Phase 3: The Orchestration (Application Layer) (Next ⏳)
* **Focus:** Inbound Ports (Use Case interfaces), Outbound Ports (Repository interfaces), and Application Services.
* **Rule:** Mocks are used for Outbound Ports during testing.

### Phase 4: The Edges (Adapters Layer)
* **Focus:** REST Controllers (Web In), JPA Repositories (Persistence Out).
* **Rule:** Use `@WebMvcTest` for web and `@DataJpaTest` (with Testcontainers) for persistence.

### Phase 5: Hardening & Deployment
* **Focus:** Security audits, Dockerization, Spring wiring (`config` layer).