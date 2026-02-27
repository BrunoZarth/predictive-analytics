package com.codeleb.insights.domain.model;

/**
 * Purpose: Represents the core domain model for a financial or business transaction.
 * Design Patterns: Rich Domain Model (encapsulation of behavior and state).
 * Expected Inputs/Outputs: Domain-specific attributes (e.g., amount, timestamp, merchant).
 * Strict Constraints: Use Java 17 record if immutable, or class with no Spring/JPA annotations. Domain layer depends on NOTHING.
 */
public record Transaction() {
}
