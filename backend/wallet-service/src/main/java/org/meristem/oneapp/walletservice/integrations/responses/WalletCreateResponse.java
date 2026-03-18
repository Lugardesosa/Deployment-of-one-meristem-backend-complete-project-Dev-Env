package org.meristem.oneapp.walletservice.integrations.responses;

import java.util.List;

public record WalletCreateResponse(

        String walletId,
        String symplusAccountNo,
        List<MiddlewareWalletAccountResponse.Nuban> nubaNs,
        String status
) {
}
