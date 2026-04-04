package org.meristem.oneapp.wealthservice.domains.responses;

public record EthicalEarningsPreviewResponse(
        String matures,
        String tenor,
        String maturity,
        String rate,
        String netInterest,
        String grossInterest
) {}