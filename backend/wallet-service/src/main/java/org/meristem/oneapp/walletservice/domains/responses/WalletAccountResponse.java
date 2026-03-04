package org.meristem.oneapp.walletservice.domains.responses;

import lombok.Builder;
import org.meristem.oneapp.walletservice.integrations.responses.WalletVirtualAccountResponse;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record WalletAccountResponse(
        String walletId,
        String accountNumber,
        List<Nuban> virtualAccounts,
        String status
) {

    public record Nuban(
            String accountNumber,
            String bankCode,
            String bankName,
            Boolean isPrimary
    ) {
    }

}
