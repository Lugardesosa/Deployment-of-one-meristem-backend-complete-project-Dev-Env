package org.meristem.oneapp.trustiesservice.domains.requests;


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
public class RealEstateRequest extends AssetRequest {

    @Schema(description = "Type of property", example = "Residential")
    @NotBlank(message = "Cannot be blank")
    private String propertyType;

    @Schema(description = "Description of the property", example = "A 3-bedroom apartment in Lekki")
    @NotBlank(message = "Cannot be blank")
    @Size( max = 300, message = "Cannot be more than 300 chars")
    private String propertyDescription;

    @Schema(description = "Address of the property", example = "12 Lekki Phase 1, Lagos")
    @NotBlank(message = "Cannot be blank")
    @Size( max = 300, message = "Cannot be more than 300 chars")
    private String propertyAddress;
}
