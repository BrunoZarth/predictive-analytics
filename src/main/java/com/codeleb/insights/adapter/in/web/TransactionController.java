package com.codeleb.insights.adapter.in.web;

import com.codeleb.insights.application.port.in.ProcessTransactionUseCase;
import com.codeleb.insights.domain.model.Transaction;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final ProcessTransactionUseCase processTransactionUseCase;

    public TransactionController(ProcessTransactionUseCase processTransactionUseCase) {
        this.processTransactionUseCase = processTransactionUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> receiveTransaction(
            @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey,
            @Valid @RequestBody TransactionRequest request) {

        // Map DTO to Domain Model
        Transaction transaction = new Transaction(
                request.transactionId(),
                request.customerId(),
                request.category(),
                request.amount(),
                request.currency(),
                request.timestamp());

        // Delegate to Application Layer Use Case
        processTransactionUseCase.processTransaction(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
