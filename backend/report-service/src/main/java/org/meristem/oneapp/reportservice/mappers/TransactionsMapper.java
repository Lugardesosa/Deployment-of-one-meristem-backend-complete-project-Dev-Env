package org.meristem.oneapp.reportservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.kafka.dtos.TransactionEventDto;
import org.meristem.oneapp.reportservice.models.Transactions;


@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionsMapper {

    TransactionsMapper INSTANCE = Mappers.getMapper(TransactionsMapper.class);


    @Mappings({
            @Mapping(target = "transactionDate", source = "createdDate"),
            @Mapping(target = "providerTransactionDate", source = "transactionDate"),
            @Mapping(target = "transactionStatus", source = "status")
    })
    Transactions transactionEventDtoToTransactions(TransactionEventDto transactionEventDto);
}
