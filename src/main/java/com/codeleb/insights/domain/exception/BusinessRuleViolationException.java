package com.codeleb.insights.domain.exception;

/**
 * Purpose: Base exception for any business rule violation within the domain
 * layer.
 * Design Patterns: Custom Domain Exception.
 * Expected Inputs/Outputs: Descriptive error message explaining the rule
 * broken.
 * Strict Constraints: Must NOT extend Spring, JPA, or web exceptions. Pure Java
 * RuntimeException.
 */
public class BusinessRuleViolationException extends RuntimeException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
