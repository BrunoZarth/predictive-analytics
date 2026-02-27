package com.codeleb.insights.application.port.out;

import com.codeleb.insights.domain.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Outbound Port for loading transactions from the persistence mechanism.
 */
public interface LoadTransactionPort {

    /**
     * Checks if a transaction already exists to support Idempotency.
     */
    boolean existsById(String id);

    /**
     * Fetches transactions for a specific customer in a memory-safe paginated way.
     */
    Page<Transaction> findByCustomerId(String customerId, Pageable pageable);
}
