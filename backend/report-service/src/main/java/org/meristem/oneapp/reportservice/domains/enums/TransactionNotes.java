package org.meristem.oneapp.reportservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionNotes {

    MONEY_MARKET_FUND_REINVESTMENT("%s, %s MMF dividend reinvestment"),
    MONEY_MARKET_FUND_CREDITED("%s, %s MMF dividend credited"),
    MONEY_MARKET_FUND_DEBITED("%s, %s MMF dividend debited"),
    MONEY_MARKET_FUND_BOOKING("Money Market Fund Booking"),
    WALLET_FUNDING("Wallet funding"),
    WALLET_DEBITED("Wallet debited");

    private final String note;
}
