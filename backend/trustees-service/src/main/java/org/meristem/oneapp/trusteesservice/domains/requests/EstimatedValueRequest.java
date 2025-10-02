package org.meristem.oneapp.trusteesservice.domains.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.trusteesservice.domains.enums.Assets;

import java.math.BigDecimal;

public record EstimatedValueRequest(@NotNull(message = "Cannot be null") Assets asset, @NotNull(message = "Cannot be null") @Min(value = 1, message = "Cannot be less than 0") Long assetId, @NotNull(message = "Cannot be null") BigDecimal value) {
}
