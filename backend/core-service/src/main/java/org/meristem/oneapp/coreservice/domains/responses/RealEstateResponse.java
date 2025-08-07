package org.meristem.oneapp.coreservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Request object for real estate asset")
public class RealEstateResponse extends AssetResponse {

    @Schema(description = "Type of property", example = "Residential")
    private String propertyType;

    @Schema(description = "Description of the property", example = "A 3-bedroom apartment in Lekki")
    private String propertyDescription;

    @Schema(description = "Address of the property", example = "12 Lekki Phase 1, Lagos")
    private String propertyAddress;
}
