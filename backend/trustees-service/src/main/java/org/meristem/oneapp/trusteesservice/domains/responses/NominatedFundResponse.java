package org.meristem.oneapp.trusteesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@SuperBuilder
@Schema(
        description = "Response object for nominated fund plan"
)
public class NominatedFundResponse extends PlanResponse {

    @Builder.Default
    @ArraySchema(
            arraySchema = @Schema(description = "Beneficiaries designated for the plan", example = "[]"),
            schema = @Schema(implementation = PlanBeneficiariesResponse.class),
            uniqueItems = true
    )
    private Set<PlanBeneficiariesResponse> beneficiaries =  new HashSet<>();
}
