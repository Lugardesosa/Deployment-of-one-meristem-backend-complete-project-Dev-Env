package org.meristem.oneapp.trusteesservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.meristem.oneapp.trusteesservice.domains.enums.FormType;

import java.util.List;

@Schema(description = "Request object for updating forms")
@Builder
public record UpdateFormRequest(@NotEmpty(message = "Cannot be blank")
                                @Schema(description = "List of form items to add", requiredMode = Schema.RequiredMode.REQUIRED)
                                List<FormItems> itemsToAdd,
                                @NotEmpty(message = "Cannot be blank")
                                @Schema(description = "List of form items to remove", requiredMode = Schema.RequiredMode.REQUIRED)
                                List<FormItems> itemsToRemove) {

    public record FormItems(
            @Schema(description = "Position of the form item", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "Cannot be null") Integer formPosition,

            @Schema(description = "Label of the form item", example = "Name", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "Cannot be null") String label,

            @Schema(description = "Placeholder text for the form item", example = "Enter your name")
            String placeholder,

            @Schema(description = "Subtext for the form item", example = "This is optional")
            String subtext,

            @Schema(description = "Type of the form item", example = "TEXT", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "Cannot be null") FormType type,

            @Schema(description = "Order of the form item", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "Cannot be null") Integer fieldOrder,

            @Schema(description = "Indicates if the form item is mandatory (1 for true, 0 for false)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "Cannot be null") @Max(value = 1, message = "Cannot be more than 1") @Min(value = 0, message = "Cannot be more than 0") Integer mandatory,

            @Schema(description = "Text size for the form item", example = "12")
            Integer textSize,

            @Schema(description = "Default value for the form item", example = "Default Text")
            String defaultValue) {
    }
}
