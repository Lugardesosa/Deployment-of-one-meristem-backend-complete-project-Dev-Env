package org.meristem.oneapp.trusteesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Request object for money market asset")
public class MoneyMarketResponse extends AssetResponse {

    @Schema(description = "Type of money market asset", example = "Treasury Bill")
    private String assetType;

    @Schema(description = "Name of the investment house", example = "Stanbic IBTC")
    private String investmentHouse;
}
