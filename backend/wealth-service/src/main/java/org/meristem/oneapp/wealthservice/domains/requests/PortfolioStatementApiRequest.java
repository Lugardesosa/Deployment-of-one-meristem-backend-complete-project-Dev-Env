package org.meristem.oneapp.wealthservice.domains.requests;

import jakarta.validation.constraints.NotBlank;

public record PortfolioStatementApiRequest(@NotBlank(message = "customerId is required") String customerId,
                                           @NotBlank(message = "period is required") String period,
                                           @NotBlank(message = "format is required") String format) {
}
