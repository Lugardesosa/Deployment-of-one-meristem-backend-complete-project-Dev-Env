package org.meristem.oneapp.wealthservice.integrations.responses;
public record MiddlewarePlacementRateCalculateResponse(
        String matures,
        String tenor,
        String maturity,
        String rate,
        String netInterest,
        String grossInterest
) {
}