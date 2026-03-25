package org.meristem.oneapp.coreservices.wealth.domains.responses;

import lombok.Builder;
import org.meristem.oneapp.coreservices.wealth.domains.enums.InvestmentStatus;
import org.meristem.oneapp.coreservices.wealth.domains.enums.InvestmentType;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record MyInvestmentResponse(PortFolioBalanceResponse portFolioBalanceResponse, List<MyInvestments> investments) {

    @Builder
    public record PortFolioBalanceResponse(
            CurrencyBalance ng, CurrencyBalance us, CurrencyBalance uk) {

        @Builder
        public record CurrencyBalance(String currencyCode, BigDecimal balance) {}
    }

    @Builder
    public record MyInvestments(InvestmentType investmentType, InvestmentStatus investmentStatus, BigDecimal amount, Float portfolioWeight) {

    }
}
