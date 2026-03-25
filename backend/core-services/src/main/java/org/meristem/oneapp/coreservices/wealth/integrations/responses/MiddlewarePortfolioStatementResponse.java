package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewarePortfolioStatementResponse(String requestId,
                                         String customerId,
                                         String period,
                                         String status) {
}
