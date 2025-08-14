package org.meristem.oneapp.coreservice.domains.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.meristem.oneapp.coreservice.models.Currencies;
import org.meristem.oneapp.coreservice.validations.constraints.ExistsById;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AssetRequest {

    @DecimalMin(value = "0.0", inclusive = false)
    @NotNull(message = "Estimated amount cannot be null")
    @Schema(description = "Estimated amount for the cash request", example = "1000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal estimatedAmount;

    @NotNull(message = "Currency assetType cannot be blank")
    @Schema(description = "Currency assetType for the cash request", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExistsById(message = "Currency does not exist", tableName = Currencies.class)
    private Long currencyId;

    @Schema(description = "Additional details about the cash request", example = "Urgent transfer")
    private String otherDetails;
}
