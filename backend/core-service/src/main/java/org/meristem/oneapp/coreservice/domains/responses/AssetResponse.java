package org.meristem.oneapp.coreservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;


@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Response object for assets")
public class AssetResponse {

        @Schema(description = "The assetType of the asset", example = "1")
        private Long id;

        @Schema(description = "Estimated amount for the cash response", example = "1000.00")
        private BigDecimal estimatedAmount;

        @Schema(description = "Currency assetType for the cash response", example = "1")
        private Long currencyId;

        @Schema(description = "Additional details about the cash response", example = "Urgent transfer")
        private String otherDetails;
}
