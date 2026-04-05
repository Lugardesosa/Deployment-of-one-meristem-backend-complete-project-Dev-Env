package org.meristem.oneapp.walletservice.integrations.requests;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MiddlewareWithdrawalRequest(
        String walletId,
        String beneficiaryAccountNumber,
        String beneficiaryBankCode,
        String beneficiaryAccountName,
        BigDecimal amount,
        String narration,
        String requestId
) {
}
