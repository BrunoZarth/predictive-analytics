package com.codeleb.insights.application.port.in;

/**
 * Purpose: Inbound port defining the use case for processing a new transaction.
 * Design Patterns: Port (Hexagonal Architecture), Command Pattern interface.
 * Expected Inputs/Outputs: Takes transaction command/DTO, returns processing result.
 * Strict Constraints: Pure Java interface. No Spring @RestController or HTTP-specific classes.
 */
public interface ProcessTransactionUseCase {
}
