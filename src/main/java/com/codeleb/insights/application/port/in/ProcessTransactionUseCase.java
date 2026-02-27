package com.codeleb.insights.application.port.in;

import com.codeleb.insights.domain.model.Transaction;

/**
 * Inbound Port: Contract for receiving and processing customer transactions
 * safely.
 */
public interface ProcessTransactionUseCase {

    /**
     * Attempts to elegantly process the transaction.
     * Enforces Idempotency to prevent duplication.
     * 
     * @param transaction The validated Domain Record
     */
    void processTransaction(Transaction transaction);
}
