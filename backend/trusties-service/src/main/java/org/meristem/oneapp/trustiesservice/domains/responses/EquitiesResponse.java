package org.meristem.oneapp.trustiesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Schema(description = "Response object for public equities")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class EquitiesResponse extends AssetResponse {

    @Schema(description = "Type of company", example = "Private")
    private String companyType;

    @Schema(description = "Brokerage house name", example = "Meristem Securities")
    private String brokerageHouse;

    @Schema(description = "Share name", example = "Dangote Cement")
    private String shareName;

    @Schema(description = "Number of units", example = "100")
    private Integer noOfUnits;
}
