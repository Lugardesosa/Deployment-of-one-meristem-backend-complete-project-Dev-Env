package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record PortfolioActivityResponse(String customerId,
                                        String type,
                                        String description,
                                        Double amount,
                                        LocalDateTime timestamp) {
}
