package org.meristem.oneapp.trustiesservice.domains.requests;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.meristem.oneapp.trustiesservice.domains.enums.Plans;
import org.meristem.oneapp.trustiesservice.validations.constraints.ContainsEnum;

import java.util.List;

@Schema(
    name = "AddBeneficiaryRequest",
    description = "Request payload to add one or more beneficiaries to a plan.",
    example = "{\"assetType\": 12345, \"beneficiaryIds\": [101,102,103], \"planType\": \"SIMPLE_WILL\"}"
)
public record AddBeneficiaryRequest(
    @Schema(
        description = "Unique identifier of the plan. Accepts either a numeric ID or a numeric string.",
        example = "12345",
        anyOf = { Long.class }
    )
    Long planId,

    @ArraySchema(
        arraySchema = @Schema(
            description = "List of beneficiary identifiers to be added.",
            example = "[101,102,103]"
        ),
        schema = @Schema(implementation = Long.class, example = "101"),
        minItems = 1,
        uniqueItems = true
    )
    List<Long> beneficiaryIds,

    @Schema(
        description = "Type of plan to which beneficiaries are being added.",
        example = "SIMPLE_WILL",
            anyOf = Plans.class
    )
    @ContainsEnum(enumClass = Plans.class)
    String planType
) {
}
