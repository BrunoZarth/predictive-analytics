package com.codeleb.insights.adapter.out.persistence;

import com.codeleb.insights.domain.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-27T23:39:32-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (GraalVM Community)"
)
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionJpaEntity toJpaEntity(Transaction domainTransaction) {
        if ( domainTransaction == null ) {
            return null;
        }

        TransactionJpaEntity transactionJpaEntity = new TransactionJpaEntity();

        transactionJpaEntity.setTransactionId( domainTransaction.id() );
        transactionJpaEntity.setCustomerId( domainTransaction.customerId() );
        transactionJpaEntity.setCategory( domainTransaction.category() );
        transactionJpaEntity.setAmount( domainTransaction.amount() );
        transactionJpaEntity.setCurrency( domainTransaction.currency() );
        transactionJpaEntity.setTimestamp( domainTransaction.timestamp() );

        return transactionJpaEntity;
    }

    @Override
    public Transaction toDomain(TransactionJpaEntity jpaEntity) {
        if ( jpaEntity == null ) {
            return null;
        }

        String id = null;
        String customerId = null;
        String category = null;
        BigDecimal amount = null;
        String currency = null;
        LocalDateTime timestamp = null;

        id = jpaEntity.getTransactionId();
        customerId = jpaEntity.getCustomerId();
        category = jpaEntity.getCategory();
        amount = jpaEntity.getAmount();
        currency = jpaEntity.getCurrency();
        timestamp = jpaEntity.getTimestamp();

        Transaction transaction = new Transaction( id, customerId, category, amount, currency, timestamp );

        return transaction;
    }
}
