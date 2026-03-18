package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewarePortfolioStatementRequest(String customerId,
                                        String period,
                                        String format) {
}
