package org.meristem.oneapp.trustiesservice.domains.requests;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.trustiesservice.domains.enums.Assets;
import org.meristem.oneapp.trustiesservice.domains.enums.Plans;
import org.meristem.oneapp.trustiesservice.validations.constraints.ContainsEnum;

import java.util.List;

@Schema(
        name = "RemoveAssetRequest",
        description = "Request payload to remove one or more assets from a plan.",
        example = """
                {
                  "id": 98765,
                  "assetIds": [101, 102, 103],
                  "planType": "SIMPLE_WILL"
                }
                """
)
public record RemoveAssetRequest(
        @Schema(description = "Unique identifier of the plan from which the assets should be removed.", example = "98765")
        @NotNull(message = "Cannot be null")
        Long planId,

        @ArraySchema(
                arraySchema = @Schema(description = "List of asset identifiers to remove.", example = "[101, 102, 103]"),
                schema = @Schema(implementation = Long.class)
        )
        @NotEmpty(message = "Cannot be empty")
        List<@NotNull Long> assetIds,

        @NotBlank(message = "Cannot be blank")
        @ContainsEnum(enumClass = Plans.class)
        @Schema(description = "Type of the plan.", example = "SIMPLE_WILL", anyOf = Plans.class)
        String planType,

        @NotBlank(message = "Cannot be blank")
        @ContainsEnum(enumClass = Assets.class)
        @Schema(description = "Type of the asset.", example = "CASH", anyOf = Assets.class)
        String assetType

) {
}
