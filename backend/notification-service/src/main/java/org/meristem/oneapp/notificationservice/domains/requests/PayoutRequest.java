package org.meristem.oneapp.notificationservice.domains.requests;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record PayoutRequest(
        String transactionId,
        String walletId,
        String status,
        BigDecimal amount,
        String currency,
        Instant createdAt,
        String statusDescription
) {
}