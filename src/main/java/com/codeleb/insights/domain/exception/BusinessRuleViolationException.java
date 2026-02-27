package com.codeleb.insights.domain.exception;

/**
 * Custom unchecked exception for business rule violations in the Domain layer.
 * Thrown, for example, when an entity is instantiated with invalid state.
 */
public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }

    public BusinessRuleViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
