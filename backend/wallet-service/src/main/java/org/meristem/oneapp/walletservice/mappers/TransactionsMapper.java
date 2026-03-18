package org.meristem.oneapp.walletservice.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.kafka.dtos.TransactionEventDto;
import org.meristem.oneapp.walletservice.domains.requests.ProvidusAccountFundedEventRequest;
import org.meristem.oneapp.walletservice.domains.responses.TransactionResponse;
import org.meristem.oneapp.walletservice.integrations.responses.WalletTransactionResponse;
import org.meristem.oneapp.walletservice.models.Transactions;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionsMapper {

    TransactionsMapper INSTANCE = Mappers.getMapper(TransactionsMapper.class);

    @Mappings({
            @Mapping(target = "sendersAccountNumber", source = "data.originatorAccountNumber"),
            @Mapping(target = "amount", source = "data.amount"),
            @Mapping(target = "sendersName", source = "data.originatorAccountName"),
            @Mapping(target = "narration", source = "data.narration"),
            @Mapping(target = "providerReference", source = "data.sessionId"),
            @Mapping(target = "createdDate", source = "data.paidAt"),

            // The following will need to be set manually after mapping or configured with @Mapping constants/defaults
            @Mapping(target = "sendersBankName", ignore = true),
            @Mapping(target = "sendersBankCode", ignore = true),
            @Mapping(target = "reference", ignore = true),
            @Mapping(target = "walletId", ignore = true),
            @Mapping(target = "virtualAccountId", ignore = true),
            @Mapping(target = "previousBalance", ignore = true),
            @Mapping(target = "newBalance", ignore = true),
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "transactionDate", ignore = true),
            @Mapping(target = "method", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastModifiedDate", ignore = true),
            @Mapping(target = "lastModifiedBy", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    Transactions providusTransactionsToTransactions(ProvidusAccountFundedEventRequest request);


    @Mappings({
            @Mapping(target = "accountName", ignore = true),
            @Mapping(target = "accountNumber", ignore = true),
            @Mapping(target = "metadata", ignore = true),
            @Mapping(target = "type", ignore = true)
    })
    TransactionEventDto transactionsToTransactionEventDto(Transactions transactions);

    List<TransactionResponse> walletTransactionResponseToTransactionResponse(List<WalletTransactionResponse> response);
    TransactionResponse walletTransactionResponseToTransactionResponse(WalletTransactionResponse response);
}
