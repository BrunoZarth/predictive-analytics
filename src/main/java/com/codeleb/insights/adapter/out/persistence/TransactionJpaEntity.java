package com.codeleb.insights.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA Entity strictly bound to the Persistence Adapter layer.
 * Not to be confused or leaked into the Domain Transaction record.
 */
@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionJpaEntity {

    @Id
    private String transactionId;

    private String customerId;

    private String category;

    private BigDecimal amount;

    private String currency;

    private LocalDateTime timestamp;
}
