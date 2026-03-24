package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewarePortfolioResponse(String fundId,
                                String fundName,
                                Double units,
                                Double value) {
}
