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
@Schema(description = "Response object for personal  asset")
public class PersonalAssetsResponse extends AssetResponse {

    @Schema(description = "Type of personal asset", example = "Jewelry")
    private String assetType;

    @Schema(description = "Description of the personal asset", example = "Gold necklace")
    private String assetDescription;

    @Schema(description = "Identifying number for the asset", example = "JN-2025-001")
    private String identifyingNo;
}
