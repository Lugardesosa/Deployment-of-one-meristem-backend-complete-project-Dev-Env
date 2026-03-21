package org.meristem.oneapp.wealthservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TBillPurchaseApiRequest(@NotBlank(message = "instrumentId is required") String instrumentId,
                                      @NotNull(message = "amount is required") @Positive(message = "amount must be greater than zero") Double amount,
                                      @NotBlank(message = "customerId is required") String customerId) {
}
