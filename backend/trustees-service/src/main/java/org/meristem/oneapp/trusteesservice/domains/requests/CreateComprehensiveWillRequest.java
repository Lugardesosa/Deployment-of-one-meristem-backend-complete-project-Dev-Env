package org.meristem.oneapp.trusteesservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.meristem.oneapp.trusteesservice.domains.enums.MarriageType;
import org.meristem.oneapp.trusteesservice.domains.enums.Religion;
import org.meristem.oneapp.trusteesservice.domains.enums.YesOrNo;
import org.meristem.oneapp.trusteesservice.validations.constraints.ContainsEnum;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(
        name = "ComprehensiveWillRequest",
        description = "Request payload containing comprehensive will information.",
        example = """
                {
                  "ownerId": 1,
                  "marriageType": "ISLAMIC_MARRIAGE",
                  "religion": "CHRISTIANITY",
                  "occupation": "Software Engineer",
                  "customaryTradition": "YES",
                  "traditionDetails": "Follows XYZ tradition",
                  "otherDetails": "Additional notes"
                }"""
)
public class CreateComprehensiveWillRequest extends CreateWillRequest {

    @Schema(description = "Owner's id if created by an admin", example = "1")
    private Long ownerId;

    @NotBlank(message = "Not blank")
    @ContainsEnum(enumClass = MarriageType.class)
    @Schema(
            description = "Marital status/type.",
            anyOf = {MarriageType.class},
            example = "ISLAMIC_MARRIAGE"
    )
    private String marriageType;

    @NotBlank(message = "Not blank")
    @ContainsEnum(enumClass = Religion.class)
    @Schema(
            description = "Religion of the testator.",
            anyOf = {Religion.class},
            example = "CHRISTIANITY"
    )
    private String religion;

    @Size(min = 1, max = 100)
    @NotBlank(message = "Not blank")
    @Schema(
            description = "Primary occupation/profession.",
            example = "Software Engineer"
    )
    private String occupation;

    @NotBlank(message = "Not blank")
    @ContainsEnum(enumClass = YesOrNo.class)
    @Schema(
            description = "Indicates whether customary traditions apply.",
            anyOf = {YesOrNo.class},
            example = "YES"
    )
    private String customaryTradition;

    @Schema(
            description = "Details about the applicable tradition, if any.",
            example = "Follows XYZ tradition",
            nullable = true
    )
    private String traditionDetails;

    @Schema(
            description = "Any additional relevant details.",
            example = "Additional notes",
            nullable = true
    )
    private String otherDetails;
}
