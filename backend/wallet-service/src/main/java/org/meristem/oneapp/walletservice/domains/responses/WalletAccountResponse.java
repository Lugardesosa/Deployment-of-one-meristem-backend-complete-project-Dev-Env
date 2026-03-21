package org.meristem.oneapp.walletservice.domains.responses;

import lombok.Builder;
import org.meristem.oneapp.walletservice.integrations.responses.MiddlewareWalletAccountResponse;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record WalletAccountResponse(
        String walletId,
        String accountNumber,
        String symplusAccountNo,
        List<Nuban> bankAccountDetails,
        String status,
        BigDecimal availableBalance,
        String currency
) {

    public record Nuban(
            String accountNumber,
            String accountName,
            String bankCode,
            String bankName
    ) {
    }
}
