package org.meristem.oneapp.trustiesservice.domains.requests;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.trustiesservice.domains.enums.Assets;

import java.math.BigDecimal;

public record EstimatedValueRequest(@NotNull(message = "Cannot be null") Assets asset, @NotNull(message = "Cannot be null") Long assetId, @NotNull(message = "Cannot be null") BigDecimal value) {
}
