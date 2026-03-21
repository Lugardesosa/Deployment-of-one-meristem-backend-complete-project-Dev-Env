package org.meristem.oneapp.walletservice.domains.responses;


import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record WalletBalanceResponse(CurrencyBalance ng, CurrencyBalance us, CurrencyBalance uk) {

    @Builder
    public record CurrencyBalance(String currencyCode, BigDecimal balance) {}
}
