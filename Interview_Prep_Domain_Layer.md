# Interview Prep: Phase 2 - Domain Layer (Predictive Analytics)

**Role:** Staff Engineer / Architect Candidate  
**Target Audience:** Strict Enterprise Recruiters (e.g., SAP, ThoughtWorks, Netflix)  
**Topic:** Defending the Domain Layer Implementation  

---

## 1. The "Elevator Pitch": Why Zero Spring in the Domain?

**The Question:** *"I see your `Transaction` and `MovingAverageStrategy` classes don't have `@Entity`, `@Component`, or `@Data` annotations. Why did you deliberately exclude the Spring Framework from your core Domain layer?"*

**The Flawless Answer:**
> "In an enterprise environment, frameworks come and go, but the business rules remain the most valuable asset. By strictly adhering to **Hexagonal Architecture (Ports and Adapters)**, I isolated the Domain layer so that it depends on absolutely nothing but the pure Java standard library. 
> 
> If we annotate our models with `@Entity` or `@Table`, we couple our pure business logic to the JPA/Hibernate lifecycle, leading to 'anemic domain models' driven by database schemas rather than business needs. If we use `@Component` on our Strategies, we tie our calculation engine to the Spring IoC container. 
> 
> Keeping the Domain 100% pure means it is blazing fast to unit test—no Spring Context startup required—and it gives us the architectural freedom to swap out the entire framework or database tomorrow without touching a single line of business logic."

---

## 2. The Java 17 Choice: Records vs. Lombok `@Data`

**The Question:** *"You used Java 17 Records for `Transaction` and `ActionableInsight`. Wouldn't it have been easier to just use standard classes with Lombok's `@Data` or `@Builder`?"*

**The Flawless Answer:**
> "While Lombok is a great tool for reducing boilerplate in DTOs (Data Transfer Objects), it is actually an anti-pattern for our core Domain Entities in this specific context.
> 
> First, **Immutability**. In a multi-threaded predictive analytics engine, state mutations lead to race conditions. Java 17 Records give us immutable data carriers out-of-the-box (`final` fields, no setters). Once a `Transaction` is logged, its history cannot be altered.
>
> Second, **Rich Domain Validation**. By using the *Compact Constructor* of the Record, I was able to natively enforce business invariants (like `amount > 0` or `confidenceScore` boundaries) exactly at the moment of object creation. This ensures that an invalid state can never exist in memory. Lombok's `@Data` generates setters that break encapsulation, and `@Builder` requires custom workarounds to enforce invariant validation at a granular level."

---

## 3. Design Patterns: Strategy Pattern & OCP

**The Question:** *"Walk me through your design for the predictive algorithms. How does your `MovingAverageStrategy` comply with the SOLID principles?"*

**The Flawless Answer:**
> "I implemented the **Strategy Pattern** via the `PredictionStrategy` interface. Predictive analytics is inherently volatile; today the business wants a Moving Average, tomorrow they might want Simple Linear Regression or a Machine Learning model. 
> 
> This design strictly adheres to the **Open/Closed Principle (OCP)**. The engine is *closed* for modification—we don't need to add `if/else` or `switch` statements to a massive `InsightCalculator` class. But it is *open* for extension: to add a new algorithm, I simply create a new pure Java class that implements `PredictionStrategy`. The Application layer can then inject the required strategy dynamically at runtime without ever modifying the existing, tested code."

---

## 4. The "Gotcha" Questions

### Gotcha #1: The Anemic Domain Trap
**Recruiter:** *"Your `MovingAverageStrategy` does all the math. Doesn't that mean your `Transaction` record is just a dumb data container? Aren't you violating Domain-Driven Design by creating an Anemic Domain Model?"*

**How to defend it:**
> "That's a great observation, but it's a calculated separation of concerns. The `Transaction` is not entirely anemic; it holds deep invariant validation in its constructor ensuring its state is always legitimate. However, a calculation like a 'Moving Average' inherently operates on a *collection* of transactions, not a single instance. Placing collection-based mathematical algorithms inside a singular `Transaction` entity violates the Single Responsibility Principle. Therefore, a stateless pure Domain Service (`MovingAverageStrategy`) is the semantically correct place for cross-entity aggregation logic."

### Gotcha #2: Floating Point Mathematics
**Recruiter:** *"Why did you use `BigDecimal` for the transaction amount and insight value instead of `double`?"*

**How to defend it:**
> "Using `double` or `float` for financial or precise analytics data is a critical error due to the IEEE 754 floating-point binary representation, which leads to precision loss (e.g., `0.1 + 0.2 = 0.30000000000000004`). In a predictive analytics motor dealing with financial transactions, precision is paramount. `BigDecimal` allows us to control the exact scale and rounding modes (like `RoundingMode.HALF_UP` which I used in the strategy) without losing a single cent to binary approximation errors."
