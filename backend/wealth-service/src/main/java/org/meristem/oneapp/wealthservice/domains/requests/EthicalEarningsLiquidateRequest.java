package org.meristem.oneapp.wealthservice.domains.requests;

import jakarta.validation.constraints.NotBlank;

public record EthicalEarningsLiquidateRequest(
        @NotBlank(message = "placementId is required") String placementId,
        @NotBlank(message = "date is required") String date
) {}