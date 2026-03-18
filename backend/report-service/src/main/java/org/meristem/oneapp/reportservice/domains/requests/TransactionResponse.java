package org.meristem.oneapp.reportservice.domains.requests;


import lombok.Builder;
import org.meristem.oneapp.reportservice.domains.enums.ActivityType;
import org.meristem.oneapp.reportservice.domains.enums.TransactionDirection;
import org.meristem.oneapp.reportservice.domains.enums.TransactionSubject;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(
        Long id,

        LocalDateTime createdDate,
        String createdBy,
        LocalDateTime lastModifiedDate,
        String lastModifiedBy,
        Integer version,
        Integer status,

        Long walletId,
        Long userId,

        ActivityType activityType,
        TransactionDirection transactionDirection,
        TransactionSubject transactionSubject,

        BigDecimal amount,
        String reference,
        String currency,

        LocalDateTime transactionDate,
        BigDecimal previousBalance,
        BigDecimal newBalance
) {
}
