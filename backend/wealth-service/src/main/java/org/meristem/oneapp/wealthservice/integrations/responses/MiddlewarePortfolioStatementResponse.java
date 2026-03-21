package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewarePortfolioStatementResponse(String requestId,
                                         String customerId,
                                         String period,
                                         String status) {
}
