package org.meristem.oneapp.wealthservice.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewarePortfolioActivityResponse(String customerId,
                                        String type,
                                        String description,
                                        Double amount,
                                        ZonedDateTime timestamp) {
}
