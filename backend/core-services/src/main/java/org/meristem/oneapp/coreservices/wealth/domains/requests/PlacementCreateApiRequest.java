package org.meristem.oneapp.coreservices.wealth.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlacementCreateApiRequest(@NotBlank(message = "productId is required") String productId,
                                        @NotNull(message = "amount is required") @Positive(message = "amount must be greater than zero") Double amount,
                                        @NotNull(message = "tenorDays is required") @Positive(message = "tenorDays must be greater than zero") Integer tenorDays,
                                        @NotBlank(message = "customerId is required") String customerId) {
}
