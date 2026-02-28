package com.codeleb.insights.adapter.out.persistence;

import com.codeleb.insights.application.port.out.LoadTransactionPort;
import com.codeleb.insights.application.port.out.SaveTransactionPort;
import com.codeleb.insights.domain.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TransactionJpaAdapter.class) // The adapter to be tested/implemented
class TransactionJpaAdapterTest {

    @Autowired
    private SaveTransactionPort saveTransactionPort;

    @Autowired
    private LoadTransactionPort loadTransactionPort;

    @Test
    @DisplayName("Should successfully persist a domain Transaction")
    void givenDomainTransaction_whenSave_thenPersistsCorrectly() {
        // Arrange
        Transaction transaction = new Transaction(
                "txn_persist_1",
                "SAP_000",
                "FOOD",
                new BigDecimal("50.00"),
                "USD",
                LocalDateTime.now());

        // Act
        saveTransactionPort.save(transaction);

        // Assert - We verify by checking if it exists via the load port
        boolean exists = loadTransactionPort.existsById("txn_persist_1");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should correctly identify existing and non-existing transactions for Idempotency")
    void givenTransactions_whenExistsById_thenReturnCorrectBoolean() {
        // Arrange
        Transaction existingTxn = new Transaction(
                "txn_idemp_1",
                "SAP_123",
                "SOFTWARE",
                new BigDecimal("299.99"),
                "EUR",
                LocalDateTime.now());
        saveTransactionPort.save(existingTxn);

        // Act & Assert
        assertThat(loadTransactionPort.existsById("txn_idemp_1")).isTrue();
        assertThat(loadTransactionPort.existsById("txn_idemp_999_not_found")).isFalse();
    }

    @Test
    @DisplayName("Should respect memory-safe pagination and isolate tenants")
    void givenMultipleTenantsAndTransactions_whenFindByCustomerId_thenPaginateAndIsolate() {
        // Arrange
        // Tenant 1 (SAP_001) - 5 transactions
        for (int i = 1; i <= 5; i++) {
            saveTransactionPort.save(new Transaction(
                    "txn_sap001_" + i,
                    "SAP_001",
                    "SERVICES",
                    new BigDecimal("100.00"),
                    "USD",
                    LocalDateTime.now()));
        }

        // Tenant 2 (SAP_002) - 1 transaction
        saveTransactionPort.save(new Transaction(
                "txn_sap002_1",
                "SAP_002",
                "HARDWARE",
                new BigDecimal("5000.00"),
                "USD",
                LocalDateTime.now()));

        // Act - Fetch first page of 3 items for SAP_001
        Page<Transaction> page = loadTransactionPort.findByCustomerId("SAP_001", PageRequest.of(0, 3));

        // Assert
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);

        // Assert isolation (no SAP_002 data leaked)
        boolean hasTenantLeak = page.getContent().stream()
                .anyMatch(txn -> !txn.customerId().equals("SAP_001"));
        assertThat(hasTenantLeak).isFalse();
    }
}
