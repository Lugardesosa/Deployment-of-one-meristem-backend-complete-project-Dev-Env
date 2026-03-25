package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record PortfolioActivityResponse(String customerId,
                                        String type,
                                        String description,
                                        Double amount,
                                        LocalDateTime timestamp) {
}
