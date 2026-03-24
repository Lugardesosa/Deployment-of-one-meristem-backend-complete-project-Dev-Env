package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record PortfolioStatementResponse(String requestId,
                                         String customerId,
                                         String period,
                                         String status) {
}
