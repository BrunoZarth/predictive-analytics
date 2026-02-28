package com.codeleb.insights.adapter.out.persistence;

import com.codeleb.insights.domain.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "transactionId", source = "id")
    TransactionJpaEntity toJpaEntity(Transaction domainTransaction);

    @Mapping(target = "id", source = "transactionId")
    Transaction toDomain(TransactionJpaEntity jpaEntity);
}
