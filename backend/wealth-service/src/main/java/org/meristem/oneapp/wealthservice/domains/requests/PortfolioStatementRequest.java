package org.meristem.oneapp.wealthservice.domains.requests;

public record PortfolioStatementRequest(String customerId,
                                        String period,
                                        String format) {
}
