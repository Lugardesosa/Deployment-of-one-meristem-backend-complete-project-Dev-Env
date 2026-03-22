package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record PortfolioAssetClassBreakdownResponse(String assetClass,
                                                   Double value,
                                                   Double allocation) {
}
