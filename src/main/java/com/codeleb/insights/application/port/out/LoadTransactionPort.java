package com.codeleb.insights.application.port.out;

/**
 * Purpose: Outbound port for retrieving transaction data from external storage.
 * Design Patterns: Port (Hexagonal Architecture), Repository Interface
 * (Domain-centric).
 * Expected Inputs/Outputs: ID or criteria as input, Domain Transaction model as
 * output.
 * Strict Constraints: Pure Java interface. No JPA annotations or SQL-specific
 * dependencies.
 */
public interface LoadTransactionPort {
}
