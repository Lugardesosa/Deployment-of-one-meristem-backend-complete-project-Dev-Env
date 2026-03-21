package org.meristem.oneapp.usersservice.integrations.responses;

import lombok.Builder;

import java.util.List;

@Builder
public record MiddlewareWalletAccountResponse(String walletId,
                                              String symplusAccountNo,
                                              List<Nuban> nubaNs,
                                              String status) {

    public record Nuban(
            String accountNumber,
            String bankCode,
            String bankName,
            Boolean isPrimary
    ) {
    }
}
