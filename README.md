# 📈 Predictive Analytics Service

[![Java 17](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Architecture: Hexagonal](https://img.shields.io/badge/Architecture-Hexagonal-orange.svg)](https://alistair.cockburn.us/hexagonal-architecture/)
[![Code Quality: TDD](https://img.shields.io/badge/Code%20Quality-TDD-success.svg)](#)

## 📌 Overview
**Predictive Analytics** is an enterprise-grade RESTful microservice built to process historical business transactions, apply predictive algorithms (e.g., Moving Average, Simple Linear Regression), and generate actionable business insights.

This project was developed with a strict focus on **maintainability, scalability, and extreme testability**, serving as a showcase of modern software engineering practices tailored for complex corporate environments.

## 🏗️ Architecture: Ports and Adapters (Hexagonal)
This application strictly adheres to the **Hexagonal Architecture** pattern to ensure absolute decoupling of the core business logic from external frameworks, UI, and databases. The dependency rule is rigorously enforced: **Dependencies always point inward.**

* **Domain (`domain`):** The pure heart of the software. Contains business models and predictive strategies (Strategy Pattern). It has **zero** dependencies on Spring or any external libraries.
* **Application (`application`):** Orchestrates the business use cases. It defines **Inbound Ports** (interfaces for driving adapters) and **Outbound Ports** (interfaces for driven adapters).
* **Adapters (`adapter`):** * **In (Web):** REST Controllers that map HTTP requests to the Inbound Ports.
    * **Out (Persistence):** Spring Data JPA repositories that implement the Outbound Ports, communicating with the database.
* **Configuration (`config`):** The only layer allowed to wire everything together using Spring's dependency injection.

### Data Flow
`Client` ➡️ `Web Adapter (Controller)` ➡️ `Inbound Port (Use Case Interface)` ➡️ `Application Service` ➡️ `Domain (Prediction Logic)` ➡️ `Outbound Port (Repository Interface)` ➡️ `Persistence Adapter (JPA)` ➡️ `Database`

## 🚀 Tech Stack
* **Core:** Java 17, Spring Boot 3+
* **Persistence:** Spring Data JPA, Hibernate, PostgreSQL (Production), H2 (Local/Testing)
* **Mapping & Boilerplate:** MapStruct, Lombok
* **API Design:** OpenAPI 3 / Swagger (`springdoc-openapi`), Jakarta Bean Validation
* **Testing:** JUnit 5, Mockito, AssertJ, Testcontainers
* **Infrastructure:** Docker, Docker Compose, Maven

## 🛠️ Methodology: Spec-Driven Development (SpecDD)
This project was built using **SpecDD** and **TDD**. Before a single line of implementation logic was written:
1.  Architectural boundaries were defined via `.spec.md` contracts.
2.  Interface Stubs (Ports) were generated.
3.  Unit and Integration Tests were written covering both happy paths and edge cases (RFC 7807 Exception handling).
4.  Implementations were crafted to satisfy the tests and contracts.

## 📂 Project Structure
```text
/src/main/java/com/codeleb/insights/
├── domain/         # Pure Java: Models, Predictive Strategies
├── application/    # Orchestration: Use Cases, Inbound/Outbound Ports
├── adapter/        # I/O Implementations
│   ├── in/web/     # REST Controllers, DTOs, GlobalExceptionHandler
│   └── out/persis/ # JPA Repositories, Entities, DB Adapters
└── config/         # Spring Beans, Bean Wiring, App Configuration
```

## ⚙️ Getting Started

### Prerequisites
* **JDK 17+**
* **Maven 3.8+**
* **Docker & Docker Compose** (for PostgreSQL via Testcontainers/Compose)

### Running Locally (H2 In-Memory Database)
Ideal for fast feedback loops and local development.

```bash
./mvnw clean install
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Running for Production (PostgreSQL)

```bash
docker-compose up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## 📖 API Documentation (Swagger)
Once the application is running, the interactive OpenAPI documentation can be accessed at:

http://localhost:8080/swagger-ui.html