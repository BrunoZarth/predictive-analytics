# ⚙️ AI Orchestration & Development Workflow

This document outlines the strict Spec-Driven Development (SpecDD) and Test-Driven Development (TDD) workflow for the **Predictive Analytics** project. As the Lead Orchestrator, you must follow this exact sequence to ensure architectural integrity, extreme code quality, and zero AI hallucination.

## 👥 The Agent Roster
Know your team. Only invoke an agent for their specific domain:
* `@architect`: System design, Hexagonal boundaries, and writing `.spec.md` contracts.
* `@qa-tester`: Writing failing tests (RED phase) based strictly on specs.
* `@data-domain`: Writing pure Java business/predictive logic (Domain layer).
* `@java-backend`: Implementing Use Cases and Persistence Adapters (Application & Adapter/Out layers).
* `@api-designer`: Implementing REST Controllers and Swagger (Adapter/In layer).
* `@tech-lead-reviewer`: Auditing code for SOLID principles and architecture violations.
* `@security-auditor`: Checking for vulnerabilities (Injection, Data Leaks).
* `@devops-engineer`: Docker, CI/CD, and Spring Profiles.
* `@professor-mentor`: Explaining the "Why" for technical interview preparation (e.g., SAP).

---

## 🔄 The Master TDD Loop (The Micro-Flow)
For every single feature or component, strictly follow this 5-step loop:

### Step 1: SPECIFY (The Contract)
Before any code is written, the boundary must be defined.
* **Prompt:** *"@architect review the requirements for [Feature Name] and update the `_layer.spec.md` and Java stubs for this context. Define the interfaces/ports."*

### Step 2: RED (The Test)
Never write implementation code without a failing test.
* **Prompt:** *"@qa-tester based on the `[Layer].spec.md` and the stubs, write the tests for [Class/Component]. Focus on boundary conditions and exceptions."*
* *Action:* Run the test. Verify it fails.

### Step 3: GREEN (The Implementation)
Call the specialist for that specific layer to make the test pass.
* **Prompt (Domain):** *"@data-domain write the pure Java implementation for [Class] to make the `@qa-tester` tests pass."*
* **Prompt (App/Adapter):** *"@java-backend / @api-designer write the implementation for [Class] to make the tests pass. Strictly follow the interface contract."*
* *Action:* Run the test. Verify it passes.

### Step 4: REFACTOR & REVIEW (The Audit)
Do not merge or move on without a code review.
* **Prompt:** *"@tech-lead-reviewer review the implementation of [Class]. Check for Hexagonal architecture violations, code smells, and SOLID compliance. Approve or request changes."*
* *Action:* If changes are requested, apply them and ensure tests still pass.

### Step 5: MENTORSHIP (The Interview Prep)
Consolidate your knowledge for technical interviews.
* **Prompt:** *"@professor-mentor explain the design patterns and architectural decisions we just used in [Component]. Give me an elevator pitch for a Senior Engineering interview at SAP."*

---

## 🏗️ Macro-Phases of Development (Inside-Out)
Because we are using Hexagonal Architecture, we build from the inside out. Do not jump layers.

### Phase 1: Architecture Skeleton (Current Phase)
* Generate the folder structure.
* Generate all `_layer.spec.md` files.
* Generate empty Java files (stubs) with Javadoc contracts.

### Phase 2: The Core (Domain Layer)
* **Focus:** Pure business rules, predictive algorithms (Strategy pattern), rich domain models.
* **Agents:** `@qa-tester` ➡️ `@data-domain` ➡️ `@tech-lead-reviewer`.
* **Rule:** ZERO Spring or external dependencies.

### Phase 3: The Orchestration (Application Layer)
* **Focus:** Inbound Ports (Use Case interfaces), Outbound Ports (Repository interfaces), and Application Services.
* **Agents:** `@qa-tester` ➡️ `@java-backend` ➡️ `@tech-lead-reviewer`.
* **Rule:** Mocks are used for Outbound Ports during testing.

### Phase 4: The Edges (Adapters Layer)
* **Focus:** REST Controllers (Web In), JPA Repositories (Persistence Out).
* **Agents:** * Web: `@qa-tester` ➡️ `@api-designer` ➡️ `@tech-lead-reviewer`.
    * Persistence: `@qa-tester` ➡️ `@java-backend` ➡️ `@tech-lead-reviewer`.
* **Rule:** Use `@WebMvcTest` for web and `@DataJpaTest` (with Testcontainers) for persistence.

### Phase 5: Hardening & Deployment
* **Focus:** Security audits, Dockerization, Spring wiring (`config` layer).
* **Agents:** `@security-auditor` and `@devops-engineer`.