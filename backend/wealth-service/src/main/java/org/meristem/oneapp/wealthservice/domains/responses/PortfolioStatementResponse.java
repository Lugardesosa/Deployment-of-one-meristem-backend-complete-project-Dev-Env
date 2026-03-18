package org.meristem.oneapp.wealthservice.domains.responses;

public record PortfolioStatementResponse(String requestId,
                                         String customerId,
                                         String period,
                                         String status) {
}
