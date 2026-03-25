package org.meristem.oneapp.coreservices.wealth.domains.requests;

public record PortfolioStatementRequest(String customerId,
                                        String period,
                                        String format) {
}
