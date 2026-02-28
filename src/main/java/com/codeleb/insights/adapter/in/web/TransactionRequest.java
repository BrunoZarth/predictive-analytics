package com.codeleb.insights.adapter.in.web;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequest(
        @NotBlank(message = "transactionId is required") String transactionId,

        @NotBlank(message = "customerId is required") String customerId,

        @NotBlank(message = "category is required") String category,

        @NotNull(message = "amount is required") @DecimalMin(value = "0.01", message = "amount must be greater than zero") BigDecimal amount,

        @NotBlank(message = "currency is required") String currency,

        @NotNull(message = "timestamp is required") LocalDateTime timestamp) {
}
