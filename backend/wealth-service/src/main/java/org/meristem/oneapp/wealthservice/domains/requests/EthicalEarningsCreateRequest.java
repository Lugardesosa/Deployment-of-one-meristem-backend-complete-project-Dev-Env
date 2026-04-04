package org.meristem.oneapp.wealthservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EthicalEarningsCreateRequest(
        @NotBlank(message = "productId is required") String productId,
        @NotBlank(message = "symplusAccountNo is required") String symplusAccountNo,
        @NotBlank(message = "date is required") String date,
        @NotNull(message = "tenorInDays is required") @Positive(message = "tenorInDays must be greater than zero") Integer tenorInDays,
        @NotNull(message = "amount is required") @Positive(message = "amount must be greater than zero") Double amount,
        @NotBlank(message = "rollover is required") String rollover
) {}