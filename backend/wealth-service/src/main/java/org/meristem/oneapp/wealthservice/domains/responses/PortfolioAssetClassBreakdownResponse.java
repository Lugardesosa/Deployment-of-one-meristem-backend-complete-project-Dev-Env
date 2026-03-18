package org.meristem.oneapp.wealthservice.domains.responses;

public record PortfolioAssetClassBreakdownResponse(String assetClass,
                                                   Double value,
                                                   Double allocation) {
}
