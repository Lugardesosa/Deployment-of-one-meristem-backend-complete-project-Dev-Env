package org.meristem.oneapp.walletservice.integrations.responses;

import java.math.BigDecimal;
import java.time.Instant;

public record MiddlewareWithdrawalStatusResponse(

        String transactionId,
        String walletId,
        String status,
        BigDecimal amount,
        String currency,
        Instant createdAt,
        String statusDescription
) {
}
