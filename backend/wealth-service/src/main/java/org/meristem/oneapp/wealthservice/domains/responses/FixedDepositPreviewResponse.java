package org.meristem.oneapp.wealthservice.domains.responses;

public record FixedDepositPreviewResponse(
        String matures,
        String tenor,
        String maturity,
        String rate,
        String netInterest,
        String grossInterest,
        String reInvestmentPercentage
) {}