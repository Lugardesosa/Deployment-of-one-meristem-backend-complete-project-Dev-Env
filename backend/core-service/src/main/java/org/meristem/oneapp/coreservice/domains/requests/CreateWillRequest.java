package org.meristem.oneapp.coreservice.domains.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.meristem.oneapp.coreservice.domains.enums.MaritalStatus;
import org.meristem.oneapp.coreservice.validations.constraints.ContainsEnum;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(
        name = "WillRequest",
        description = "Request DTO for creating or updating a Will.",
        example = "{\"lastName\":\"Doe\",\"firstName\":\"John\",\"middleName\":\"A\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"+2348012345678\",\"address\":\"12, Adeola Odeku St, Victoria Island, Lagos\",\"title\":\"Mr\",\"maritalStatus\":\"MARRIED\",\"assetIds\":{},\"beneficiaryIds\":[101,102],\"willExecutorRequests\":[{\"willExecutorName\":\"Jane Doe\",\"willExecutorAddress\":\"34, Marina Rd, Lagos\"}]}"
)
public class CreateWillRequest extends EstatePlanRequest {

    @Schema(
            description = "Surname / Family name.",
            example = "Doe",
            minLength = 1,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 100)
    private String lastName;

    @Schema(
            description = "Given name.",
            example = "John",
            minLength = 1,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 100)
    private String firstName;

    @Schema(
            description = "Middle name or initial, if applicable.",
            example = "A."
    )
    private String middleName;

    @Schema(
            description = "Contact email address.",
            example = "john.doe@example.com",
            minLength = 1,
            maxLength = 300,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 300)
    private String email;

    @Schema(
            description = "Contact phone number in E.164 or local format.",
            example = "+2348012345678",
            minLength = 1,
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 50)
    private String phoneNumber;

    @Schema(
            description = "Residential address.",
            example = "12, Adeola Odeku St, Victoria Island, Lagos",
            minLength = 1,
            maxLength = 300,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 300)
    private String address;

    @Schema(
            description = "Personal title.",
            example = "Mr",
            minLength = 1,
            maxLength = 20,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 20)
    private String title;

    @Schema(
            description = "Marital status as an enum name.",
            example = "MARRIED",
            minLength = 1,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Not blank")
    @Size(min = 1, max = 100)
    @ContainsEnum(enumClass = MaritalStatus.class)
    private String maritalStatus;

    @Schema(
            description = "Container for asset identifier(s) selected for the will.",
            implementation = AddAssetRequest.class,
            example = "{}"
    )
    @NotNull(message = "Cannot be null")
    @Valid
    private AddAssetRequest assetIds;

    @ArraySchema(
            arraySchema = @Schema(
                    description = "IDs of beneficiaries associated with the will.",
                    example = "[101,102]"
            ),
            schema = @Schema(implementation = Long.class)
    )
    @Builder.Default
    @NotEmpty(message = "Cannot be empty")
    private List<@NotNull Long> beneficiaryIds = new ArrayList<>();

    @ArraySchema(
            arraySchema = @Schema(
                    description = "List of will executor entries.",
                    example = "[{\"willExecutorName\":\"Jane Doe\",\"willExecutorAddress\":\"34, Marina Rd, Lagos\"}]"
            ),
            schema = @Schema(implementation = WillExecutorRequest.class)
    )
    @Builder.Default
    @NotEmpty(message = "Cannot be empty")
    private List<WillExecutorRequest> willExecutorRequests = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(
            name = "WillExecutorRequest",
            description = "Executor information for the will."
    )
    public static class WillExecutorRequest {

        @Schema(
                description = "Full name of the will executor.",
                example = "Jane Doe"
        )
        @NotBlank(message = "Cannot be blank")
        private String willExecutorName;

        @Schema(
                description = "Address of the will executor.",
                example = "34, Marina Rd, Lagos"
        )
        @NotBlank(message = "Cannot be blank")
        private String willExecutorAddress;

    }
}
