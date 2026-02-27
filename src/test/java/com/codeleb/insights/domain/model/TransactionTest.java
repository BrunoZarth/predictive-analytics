package com.codeleb.insights.domain.model;

import com.codeleb.insights.domain.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionTest {

    @Test
    void givenValidData_whenCreatingTransaction_thenSuccess() {
        // Arrange
        String id = "txn_12345";
        String customerId = "cust_999";
        String category = "FOOD";
        BigDecimal amount = new BigDecimal("150.00");
        String currency = "USD";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        Transaction transaction = new Transaction(id, customerId, category, amount, currency, timestamp);

        // Assert
        assertThat(transaction).isNotNull();
        assertThat(transaction.id()).isEqualTo(id);
        assertThat(transaction.customerId()).isEqualTo(customerId);
        assertThat(transaction.category()).isEqualTo(category);
        assertThat(transaction.amount()).isEqualTo(amount);
        assertThat(transaction.currency()).isEqualTo(currency);
        assertThat(transaction.timestamp()).isEqualTo(timestamp);
    }

    @Test
    void givenMissingCustomerId_whenCreatingTransaction_thenThrowsBusinessRuleViolationException() {
        // Arrange
        String id = "txn_12345";
        BigDecimal amount = new BigDecimal("150.00");
        String currency = "USD";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act & Assert
        assertThatThrownBy(() -> new Transaction(id, "  ", "FOOD", amount, currency, timestamp))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("customerId");
    }

    @Test
    void givenMissingCategory_whenCreatingTransaction_thenThrowsBusinessRuleViolationException() {
        // Arrange
        String id = "txn_12345";
        String customerId = "cust_999";
        BigDecimal amount = new BigDecimal("150.00");
        String currency = "USD";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act & Assert
        assertThatThrownBy(() -> new Transaction(id, customerId, "", amount, currency, timestamp))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("category");
    }

    @Test
    void givenNegativeAmount_whenCreatingTransaction_thenThrowsBusinessRuleViolationException() {
        // Arrange
        String id = "txn_12345";
        String customerId = "cust_999";
        String category = "FOOD";
        BigDecimal negativeAmount = new BigDecimal("-50.00");
        String currency = "USD";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act & Assert
        assertThatThrownBy(() -> new Transaction(id, customerId, category, negativeAmount, currency, timestamp))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("amount");
    }

    @Test
    void givenZeroAmount_whenCreatingTransaction_thenThrowsBusinessRuleViolationException() {
        // Arrange
        String id = "txn_12345";
        String customerId = "cust_999";
        String category = "FOOD";
        BigDecimal zeroAmount = BigDecimal.ZERO;
        String currency = "USD";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act & Assert
        assertThatThrownBy(() -> new Transaction(id, customerId, category, zeroAmount, currency, timestamp))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("amount");
    }

    @Test
    void givenMissingCurrency_whenCreatingTransaction_thenThrowsBusinessRuleViolationException() {
        // Arrange
        String id = "txn_12345";
        String customerId = "cust_999";
        String category = "FOOD";
        BigDecimal amount = new BigDecimal("100.00");
        LocalDateTime timestamp = LocalDateTime.now();

        // Act & Assert
        assertThatThrownBy(() -> new Transaction(id, customerId, category, amount, null, timestamp))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("currency");
    }

    @Test
    void givenNullTimestamp_whenCreatingTransaction_thenThrowsBusinessRuleViolationException() {
        // Arrange
        String id = "txn_12345";
        String customerId = "cust_999";
        String category = "FOOD";
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "USD";

        // Act & Assert
        assertThatThrownBy(() -> new Transaction(id, customerId, category, amount, currency, null))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("timestamp");
    }
}
