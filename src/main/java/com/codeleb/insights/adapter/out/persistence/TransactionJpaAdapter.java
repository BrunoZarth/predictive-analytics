package com.codeleb.insights.adapter.out.persistence;

import com.codeleb.insights.application.port.out.LoadTransactionPort;
import com.codeleb.insights.application.port.out.SaveTransactionPort;
import com.codeleb.insights.domain.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class TransactionJpaAdapter implements SaveTransactionPort, LoadTransactionPort {

    private final TransactionJpaRepository repository;
    private final TransactionMapper mapper = TransactionMapper.INSTANCE;

    public TransactionJpaAdapter(TransactionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Transaction> findByCustomerId(String customerId, Pageable pageable) {
        return repository.findByCustomerId(customerId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(String transactionId) {
        return repository.existsById(transactionId);
    }

    @Override
    public void save(Transaction transaction) {
        TransactionJpaEntity entity = mapper.toJpaEntity(transaction);
        repository.save(entity);
    }
}
