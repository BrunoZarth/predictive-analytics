package com.codeleb.insights.adapter.in.web;

import com.codeleb.insights.application.port.in.ProcessTransactionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessTransactionUseCase processTransactionUseCase;

    @Test
    void givenValidPayloadAndIdempotencyKey_whenReceiveTransaction_thenReturns201AndInvokesUseCase() throws Exception {
        // Arrange
        String validJson = """
                {
                  "transactionId": "txn_123",
                  "customerId": "cust_456",
                  "category": "SOFTWARE",
                  "amount": 150.00,
                  "currency": "USD",
                  "timestamp": "2023-10-27T10:00:00"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                .header("Idempotency-Key", "idemp_abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                .andExpect(status().isCreated());

        // Verify the application boundary was crossed exactly once
        verify(processTransactionUseCase, times(1)).processTransaction(any());
    }

    @Test
    void givenMissingIdempotencyKey_whenReceiveTransaction_thenReturns400() throws Exception {
        // Arrange
        String validJson = "{ \"transactionId\":\"txn_123\", \"customerId\":\"c_1\", \"category\":\"FOOD\", \"amount\":10.0, \"currency\":\"USD\", \"timestamp\":\"2023-10-27T10:00:00\" }";

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                // Missing Idempotency-Key header explicitly
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidPayload_whenReceiveTransaction_thenReturns400() throws Exception {
        // Arrange
        // Invalid payload: negative amount and missing currency
        String invalidJson = """
                {
                  "transactionId": "txn_123",
                  "customerId": "cust_456",
                  "category": "SOFTWARE",
                  "amount": -50.00,
                  "timestamp": "2023-10-27T10:00:00"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                .header("Idempotency-Key", "idemp_abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
