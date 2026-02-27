package com.codeleb.insights.domain.model;

import com.codeleb.insights.domain.exception.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Purpose: Represents a rich Domain Entity for a financial or business
 * transaction.
 * Design Patterns: Rich Domain Model (encapsulation of behavior and state).
 * Expected Inputs/Outputs: Deep business metadata such as customerId, category,
 * and currency.
 * Strict Constraints: Use Java 17 record. Domain layer depends on NOTHING.
 */
public record Transaction(
        String id,
        String customerId,
        String category,
        BigDecimal amount,
        String currency,
        LocalDateTime timestamp) {
    public Transaction {
        if (id == null || id.isBlank()) {
            throw new BusinessRuleViolationException("Transaction id cannot be null or blank");
        }
        if (customerId == null || customerId.isBlank()) {
            throw new BusinessRuleViolationException("Transaction customerId cannot be null or blank");
        }
        if (category == null || category.isBlank()) {
            throw new BusinessRuleViolationException("Transaction category cannot be null or blank");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleViolationException("Transaction amount must be greater than zero");
        }
        if (currency == null || currency.isBlank()) {
            throw new BusinessRuleViolationException("Transaction currency cannot be null or blank");
        }
        if (timestamp == null) {
            throw new BusinessRuleViolationException("Transaction timestamp cannot be null");
        }
    }
}
