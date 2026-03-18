package org.meristem.oneapp.walletservice.integrations.responses;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record MiddlewareWalletAccountResponse(
        String walletId,
        String currency,
        BigDecimal availableBalance,
        String symplusAccountNo,
        List<Nuban> bankAccountDetails,
        String status
) {

    public record Nuban(
            String accountNumber,
            String bankCode,
            String bankName,
            String accountName
    ) {
    }
}
