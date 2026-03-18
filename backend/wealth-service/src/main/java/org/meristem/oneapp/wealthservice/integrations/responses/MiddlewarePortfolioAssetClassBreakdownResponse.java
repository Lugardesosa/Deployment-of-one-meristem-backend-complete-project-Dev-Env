package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewarePortfolioAssetClassBreakdownResponse(String assetClass,
                                                   Double value,
                                                   Double allocation) {
}
