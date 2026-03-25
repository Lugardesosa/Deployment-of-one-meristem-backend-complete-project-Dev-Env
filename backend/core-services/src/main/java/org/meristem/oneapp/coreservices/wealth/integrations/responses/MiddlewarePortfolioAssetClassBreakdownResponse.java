package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewarePortfolioAssetClassBreakdownResponse(String assetClass,
                                                   Double value,
                                                   Double allocation) {
}
