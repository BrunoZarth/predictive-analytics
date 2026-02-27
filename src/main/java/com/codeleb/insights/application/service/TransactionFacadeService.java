package com.codeleb.insights.application.service;

import com.codeleb.insights.application.exception.DuplicateTransactionException;
import com.codeleb.insights.application.port.in.GenerateInsightUseCase;
import com.codeleb.insights.application.port.in.ProcessTransactionUseCase;
import com.codeleb.insights.application.port.out.LoadTransactionPort;
import com.codeleb.insights.application.port.out.SaveTransactionPort;
import com.codeleb.insights.domain.model.ActionableInsight;
import com.codeleb.insights.domain.model.Transaction;
import com.codeleb.insights.domain.service.PredictionStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Note: Pure Java or DI annotation allowed here.
 * Since Application is strictly decoupled from the web infrastructure, we can
 * use
 * JSR-330 @Named or nothing (configured manually in Config layer).
 * For compilation purity and Hexagonal strictness, it relies purely on
 * constructor injection.
 */
public class TransactionFacadeService implements ProcessTransactionUseCase, GenerateInsightUseCase {

    private final SaveTransactionPort saveTransactionPort;
    private final LoadTransactionPort loadTransactionPort;
    private final PredictionStrategy predictionStrategy;

    public TransactionFacadeService(
            SaveTransactionPort saveTransactionPort,
            LoadTransactionPort loadTransactionPort,
            PredictionStrategy predictionStrategy) {
        this.saveTransactionPort = saveTransactionPort;
        this.loadTransactionPort = loadTransactionPort;
        this.predictionStrategy = predictionStrategy;
    }

    @Override
    public void processTransaction(Transaction transaction) {
        // Enforce Idempotency
        if (loadTransactionPort.existsById(transaction.id())) {
            throw new DuplicateTransactionException(
                    String.format("Idempotency violation: Transaction ID %s already processed.", transaction.id()));
        }

        saveTransactionPort.save(transaction);
    }

    @Override
    public List<ActionableInsight> generateForCustomer(String customerId) {
        // Enforce Memory Safe Pagination (Outbound read)
        // Hardcoding a page constraint for demonstration/safety. A real app might
        // parameterize this.
        Pageable defaultPage = PageRequest.of(0, 100);

        Page<Transaction> transactionPage = loadTransactionPort.findByCustomerId(customerId, defaultPage);

        // Delegate to pure Domain Engine
        return predictionStrategy.predict(transactionPage.getContent());
    }
}
