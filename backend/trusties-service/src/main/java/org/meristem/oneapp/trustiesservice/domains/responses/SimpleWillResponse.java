package org.meristem.oneapp.trustiesservice.domains.responses;


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
@Schema(
        description = "Response object for simple will plan"
)
public class SimpleWillResponse extends PlanResponse {

    @Schema(description = "Human-friendly title of the will plan", example = "Mr")
    private String title;

    @Schema(description = "Marital status of the testator at the time of plan creation", example = "SINGLE")
    private String maritalStatus;

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
