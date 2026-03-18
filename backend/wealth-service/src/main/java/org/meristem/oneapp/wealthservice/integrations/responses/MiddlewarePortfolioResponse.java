package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewarePortfolioResponse(String fundId,
                                String fundName,
                                Double units,
                                Double value) {
}
