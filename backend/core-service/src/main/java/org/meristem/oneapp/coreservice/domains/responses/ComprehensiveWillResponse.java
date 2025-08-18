package org.meristem.oneapp.coreservice.domains.responses;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Response object for comprehensive will plan")
public class ComprehensiveWillResponse extends SimpleWillResponse {

    @Schema(description = "Type of marriage", example = "Monogamous")
    private String marriageType;

    @Schema(description = "Religious affiliation of the testator", example = "Christianity")
    private String religion;

    @Schema(description = "Primary occupation or profession", example = "Software Engineer")
    private String occupation;

    @Schema(description = "Customary law or tradition observed", example = "Yoruba")
    private String customaryTradition;

    @Schema(description = "Details about how customary traditions affect will execution", example = "Inheritance follows paternal lineage customs")
    private String traditionDetails;

    @Schema(description = "Any additional information that may influence the will", example = "Includes provisions for guardianship and digital assets")
    private String otherDetails;

    @ArraySchema(
            arraySchema = @Schema(description = "Collection of assets associated with the plan", example = "[]"),
            schema = @Schema(implementation = PlanAssetResponse.class),
            uniqueItems = true
    )
    private Set<PlanAssetResponse> assets;

    @Builder.Default
    @ArraySchema(
            arraySchema = @Schema(description = "Beneficiaries designated for the plan", example = "[]"),
            schema = @Schema(implementation = PlanBeneficiariesResponse.class),
            uniqueItems = true
    )
    private Set<PlanBeneficiariesResponse> beneficiaries =  new HashSet<>();
}
