package org.meristem.oneapp.coreservices.wealth.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlacementTopUpApiRequest(@NotBlank(message = "placementId is required") String placementId,
                                       @NotNull(message = "amount is required") @Positive(message = "amount must be greater than zero") Double amount,
                                       @NotBlank(message = "customerId is required") String customerId) {
}
