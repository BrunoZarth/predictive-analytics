package com.codeleb.insights.application.exception;

/**
 * Thrown when a transaction is submitted but its Idempotency-Key or ID already
 * exists in the system.
 */
public class DuplicateTransactionException extends RuntimeException {

    public DuplicateTransactionException(String message) {
        super(message);
    }
}
