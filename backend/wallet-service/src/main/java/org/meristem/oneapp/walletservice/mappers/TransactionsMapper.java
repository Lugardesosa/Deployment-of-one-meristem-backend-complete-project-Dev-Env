package org.meristem.oneapp.walletservice.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.walletservice.domains.requests.WemaTransactionNotificationRequest;
import org.meristem.oneapp.walletservice.models.Transactions;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionsMapper {

    TransactionsMapper INSTANCE = Mappers.getMapper(TransactionsMapper.class);

    @Mappings({
            @Mapping(target = "sendersAccountNumber", source = "originatorAccountNumber"),
            @Mapping(target = "amount", source = "amount"),
            @Mapping(target = "sendersName", source = "originatorName"),
            @Mapping(target = "narration", source = "narration"),
            @Mapping(target = "providerReference", source = "paymentReference"),
            @Mapping(target = "sendersBankName", source = "sendersBankName"),
            @Mapping(target = "sendersBankCode", source = "sendersBankCode"),
            @Mapping(target = "reference", source = "sessionId"),

            // The following will need to be set manually after mapping or configured with @Mapping constants/defaults
            @Mapping(target = "walletId", ignore = true),
            @Mapping(target = "virtualAccountId", ignore = true),
            @Mapping(target = "previousBalance", ignore = true),
            @Mapping(target = "newBalance", ignore = true),
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "method", ignore = true),
            @Mapping(target = "metadata", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastModifiedDate", ignore = true),
            @Mapping(target = "lastModifiedBy", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    Transactions wemaTransactionsToTransactions(WemaTransactionNotificationRequest request);
}
