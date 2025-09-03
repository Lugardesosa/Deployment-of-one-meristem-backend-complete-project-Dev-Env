package org.meristem.oneapp.trustiesservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.trustiesservice.domains.enums.Plans;
import org.meristem.oneapp.trustiesservice.validations.constraints.ContainsEnum;

import java.util.List;

@Schema(
        name = "RemoveBeneficiaryRequest",
        description = "Request payload to remove one or more beneficiaries from a plan",
        example = "{\n  \"assetType\": 123456,\n  \"beneficiaryIds\": [111, 222, 333],\n  \"planType\": \"SIMPLE_WILL\"\n}"
)
public record RemoveBeneficiaryRequest(
        @Schema(
                description = "Unique identifier of the plan",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "123456"
        )
        @NotNull(message = "Cannot be null") Long planId,

        @Schema(
                description = "IDs of beneficiaries to be removed from the plan",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "[111, 222, 333]"
        )
        @NotEmpty(message = "Cannot be empty") List<Long> beneficiaryIds,

        @Schema(
                description = "Type of plan; must match one of the values of the Wills enum",
                requiredMode = Schema.RequiredMode.REQUIRED,
                implementation = Plans.class,
                example = "SIMPLE_WILL",
                anyOf = Plans.class
        )
        @ContainsEnum(enumClass = Plans.class)
                                       String planType) {
}
