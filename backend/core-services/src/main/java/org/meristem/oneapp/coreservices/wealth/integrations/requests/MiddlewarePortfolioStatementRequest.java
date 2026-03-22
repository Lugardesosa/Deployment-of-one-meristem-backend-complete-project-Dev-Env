package org.meristem.oneapp.coreservices.wealth.integrations.requests;

public record MiddlewarePortfolioStatementRequest(String customerId,
                                        String period,
                                        String format) {
}
