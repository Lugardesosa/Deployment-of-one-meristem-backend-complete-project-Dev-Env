package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record PortfolioResponse(String fundId,
                                String fundName,
                                Double units,
                                Double value) {
}
