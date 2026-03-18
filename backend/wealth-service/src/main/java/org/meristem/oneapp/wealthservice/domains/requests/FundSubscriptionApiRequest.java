package org.meristem.oneapp.wealthservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FundSubscriptionApiRequest(@NotBlank(message = "customerId is required") String customerId,
                                         @NotNull(message = "amount is required") @Positive(message = "amount must be greater than zero") Double amount,
                                         @NotBlank(message = "sourceAccountNo is required") String sourceAccountNo) {
}
