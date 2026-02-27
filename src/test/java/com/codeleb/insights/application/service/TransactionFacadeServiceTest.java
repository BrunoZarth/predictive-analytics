package com.codeleb.insights.application.service;

import com.codeleb.insights.application.exception.DuplicateTransactionException;
import com.codeleb.insights.application.port.out.LoadTransactionPort;
import com.codeleb.insights.application.port.out.SaveTransactionPort;
import com.codeleb.insights.domain.model.ActionableInsight;
import com.codeleb.insights.domain.model.Transaction;
import com.codeleb.insights.domain.service.PredictionStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionFacadeServiceTest {

    @Mock
    private SaveTransactionPort saveTransactionPort;

    @Mock
    private LoadTransactionPort loadTransactionPort;

    @Mock
    private PredictionStrategy predictionStrategy;

    @InjectMocks
    private TransactionFacadeService transactionFacadeService;

    @Test
    void givenValidNewTransaction_whenProcessTransaction_thenSavesToPort() {
        // Arrange
        Transaction transaction = new Transaction("txn_1", "cust_1", "FOOD", new BigDecimal("100"), "USD",
                LocalDateTime.now());
        when(loadTransactionPort.existsById(transaction.id())).thenReturn(false);

        // Act
        transactionFacadeService.processTransaction(transaction);

        // Assert
        verify(loadTransactionPort, times(1)).existsById(transaction.id());
        verify(saveTransactionPort, times(1)).save(transaction);
    }

    @Test
    void givenExistingTransaction_whenProcessTransaction_thenThrowsDuplicateTransactionException() {
        // Arrange
        Transaction transaction = new Transaction("txn_duplicate", "cust_1", "FOOD", new BigDecimal("100"), "USD",
                LocalDateTime.now());
        when(loadTransactionPort.existsById(transaction.id())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> transactionFacadeService.processTransaction(transaction))
                .isInstanceOf(DuplicateTransactionException.class)
                .hasMessageContaining("Idempotency violation");

        // Verify save was never called
        verify(saveTransactionPort, never()).save(any());
    }

    @Test
    void givenValidCustomerId_whenGenerateCustomerInsights_thenFetchesWithPaginationAndDelegatesToStrategy() {
        // Arrange
        String customerId = "cust_1";

        // Mock a paginated response from the DB
        Transaction t1 = new Transaction("txn_1", customerId, "FOOD", new BigDecimal("100"), "USD",
                LocalDateTime.now());
        Transaction t2 = new Transaction("txn_2", customerId, "FOOD", new BigDecimal("50"), "USD", LocalDateTime.now());
        PageImpl<Transaction> transactionPage = new PageImpl<>(List.of(t1, t2), PageRequest.of(0, 100), 2);

        when(loadTransactionPort.findByCustomerId(eq(customerId), any(Pageable.class)))
                .thenReturn(transactionPage);

        // Mock the domain logic outcome
        ActionableInsight insight = new ActionableInsight(customerId, "MOVING_AVERAGE_FOOD", "Avg calculated",
                new BigDecimal("75.00"), 1.0);
        when(predictionStrategy.predict(anyList())).thenReturn(List.of(insight));

        // Act
        List<ActionableInsight> result = transactionFacadeService.generateForCustomer(customerId);

        // Assert
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).customerId()).isEqualTo(customerId);

        // Ensure memory-safe pagination was used and strategy was invoked
        verify(loadTransactionPort, times(1)).findByCustomerId(eq(customerId), any(Pageable.class));
        verify(predictionStrategy, times(1)).predict(anyList());
    }
}
