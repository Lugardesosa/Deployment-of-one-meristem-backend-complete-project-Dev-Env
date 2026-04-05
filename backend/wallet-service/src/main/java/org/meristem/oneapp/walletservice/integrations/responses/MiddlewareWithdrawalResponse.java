package org.meristem.oneapp.walletservice.integrations.responses;

import java.math.BigDecimal;
import java.time.Instant;

public record MiddlewareWithdrawalResponse(

        String transactionId,
        String walletId,
        BigDecimal amount,
        String status,
        Instant createdAt,
        String message
) {
}
