package com.codeleb.insights.application.port.out;

import com.codeleb.insights.domain.model.Transaction;

/**
 * Outbound Port for saving transactions to the persistence mechanism.
 */
public interface SaveTransactionPort {
    void save(Transaction transaction);
}
