package org.meristem.oneapp.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionEventDto(
        LocalDateTime createdDate,
        Long walletId,
        Long virtualAccountId,
        String reference,
        String providerReference,
        BigDecimal amount,
        BigDecimal previousBalance,
        BigDecimal newBalance,
        String type,
        String method,
        String narration,
        Map<String, Object> metadata,
        String sendersBankName,
        String sendersBankCode,
        String sendersAccountNumber,
        String sendersName,
        Integer status,
        String accountNumber,
        String accountName,
        LocalDateTime transactionDate
) {
}
