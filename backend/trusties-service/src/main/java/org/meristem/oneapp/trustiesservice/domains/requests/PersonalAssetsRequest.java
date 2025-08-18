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
@Schema(description = "Request object for personal asset")
public class PersonalAssetsRequest extends AssetRequest {

    @Schema(description = "Type of personal asset", example = "Jewelry")
    @NotBlank(message = "Cannot be blank")
    @Size(min = 1, max = 150)
    private String assetType;

    @Schema(description = "Description of the personal asset", example = "Gold necklace")
    @NotBlank(message = "Cannot be blank")
    @Size(min = 1, max = 300)
    private String assetDescription;

    @Schema(description = "Identifying number for the asset", example = "JN-2025-001")
    @Size(min = 1, max = 150)
    @NotBlank(message = "Cannot be blank")
    private String identifyingNo;
}
