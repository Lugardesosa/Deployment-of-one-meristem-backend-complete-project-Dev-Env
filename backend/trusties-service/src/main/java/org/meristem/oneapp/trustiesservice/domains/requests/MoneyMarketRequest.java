package org.meristem.oneapp.trustiesservice.domains.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class MoneyMarketRequest extends AssetRequest {

    @Schema(description = "Type of money market asset", example = "Treasury Bill")
    @NotBlank(message = "Cannot be null")
    private String assetType;

    @Schema(description = "Name of the investment house", example = "Stanbic IBTC")
    @NotBlank(message = "Cannot be null")
    private String investmentHouse;
}
