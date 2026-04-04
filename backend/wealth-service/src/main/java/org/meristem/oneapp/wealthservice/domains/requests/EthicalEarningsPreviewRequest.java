package org.meristem.oneapp.wealthservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EthicalEarningsPreviewRequest(
        @NotBlank(message = "fundId is required") String fundId,
        @NotBlank(message = "productId is required") String productId,
        @NotBlank(message = "effectiveDate is required") String effectiveDate,
        @NotNull(message = "amount is required") @Positive(message = "amount must be greater than zero") Double amount,
        @NotNull(message = "tenorDays is required") @Positive(message = "tenorDays must be greater than zero") Integer tenorDays
) {}