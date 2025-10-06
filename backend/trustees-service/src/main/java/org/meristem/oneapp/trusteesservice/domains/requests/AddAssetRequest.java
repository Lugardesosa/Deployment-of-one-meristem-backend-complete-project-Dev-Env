package org.meristem.oneapp.trusteesservice.domains.requests;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.trusteesservice.domains.enums.Plans;
import org.meristem.oneapp.trusteesservice.validations.constraints.ContainsEnum;

import java.util.List;

@Schema(
        name = "AddAssetRequest",
        description = "Request to add one or more assets to a plan."
)
public record AddAssetRequest(
        @Schema(
                description = "The identifier of the plan to which assets will be added.",
                example = "987654321"
        )
        Long planId,

        @ArraySchema(
                schema = @Schema(implementation = AddAssetRequest.Assets.class),
                arraySchema = @Schema(
                        description = "Collection of assets to associate with the plan.",
                        example = "[{\"assetId\": 101, \"assetType\": \"CASH\"}, {\"assetId\": 202, \"assetType\": \"REAL_ESTATE\"}]"
                )
        )
        @NotEmpty(message = "Cannot be empty")
        List<@Valid Assets> assetIds,

        @ContainsEnum(enumClass = Plans.class)
        @Schema(
                description = "The type of plan. Must match one of the expected plan types.",
                anyOf = {Plans.class},
                example = "SIMPLE_WILL"
        )
        @NotBlank(message = "Cannot be blank")
        String planType) {

    @Schema(
            name = "AddAssetRequest.Assets",
            description = "Asset reference and type to be linked to the plan."
    )
    public record Assets (
            @Schema(
                    description = "The unique identifier of the asset.",
                    example = "101"
            )
            @NotNull(message = "Cannot be null")
            Long assetId,

            @Schema(
                    description = "The type/category of the asset.",
                    anyOf = {org.meristem.oneapp.trusteesservice.domains.enums.Assets.class},
                    example = "CASH"
            )
            @NotBlank(message = "Cannot be blank")
            @ContainsEnum(enumClass = org.meristem.oneapp.trusteesservice.domains.enums.Assets.class) String assetType) {}
}
