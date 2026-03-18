package org.meristem.oneapp.wealthservice.domains.responses;

public record PortfolioResponse(String fundId,
                                String fundName,
                                Double units,
                                Double value) {
}
