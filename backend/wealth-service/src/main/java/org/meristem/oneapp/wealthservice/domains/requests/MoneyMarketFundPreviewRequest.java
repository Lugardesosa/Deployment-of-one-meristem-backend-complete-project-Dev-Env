package org.meristem.oneapp.wealthservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MoneyMarketFundPreviewRequest(
        @NotBlank(message = "fundId is required") String fundId,
        @NotNull(message = "amount is required") @Positive(message = "amount must be greater than zero") Double amount
) {}