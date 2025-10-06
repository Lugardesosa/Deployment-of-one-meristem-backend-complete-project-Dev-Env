package org.meristem.oneapp.trusteesservice.domains.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Request object for updating selections")
public record UpdateSelectionRequest(
        @Schema(description = "List of items to add", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "Cannot be empty") List<Items> itemsToAdd,
        @Schema(description = "List of items to remove", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "Cannot be empty")  List<Items> itemsToRemove) {

    @Schema(description = "Item details")
    public record Items(
            @Schema(description = "Form ID of the item", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("form_id") @NotNull(message = "Cannot be nul") Long formId,
            @Schema(description = "Selection value of the item", example = "Sample Value", requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonProperty("selection_value") @NotBlank(message = "Cannot be nul") String selectionValue) {}
}
