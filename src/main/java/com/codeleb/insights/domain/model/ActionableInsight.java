package com.codeleb.insights.domain.model;

/**
 * Purpose: Encapsulates an insight generated from transaction analysis.
 * Design Patterns: Value Object / Record.
 * Expected Inputs/Outputs: Insight metrics, confidence score, and recommendation details.
 * Strict Constraints: Use Java 17 record. No framework annotations allowed. Domain depends on NOTHING.
 */
public record ActionableInsight() {
}
