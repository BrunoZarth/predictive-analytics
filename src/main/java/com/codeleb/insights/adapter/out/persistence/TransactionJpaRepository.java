package com.codeleb.insights.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository tailored for the Persistence Adapter.
 */
@Repository
public interface TransactionJpaRepository
        extends JpaRepository<TransactionJpaEntity, String>, PagingAndSortingRepository<TransactionJpaEntity, String> {

    Page<TransactionJpaEntity> findByCustomerId(String customerId, Pageable pageable);
}
