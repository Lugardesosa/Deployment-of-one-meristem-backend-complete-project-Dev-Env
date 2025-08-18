package org.meristem.oneapp.trustiesservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.meristem.oneapp.trustiesservice.domains.enums.MarriageType;
import org.meristem.oneapp.trustiesservice.domains.enums.Religion;
import org.meristem.oneapp.trustiesservice.domains.enums.YesOrNo;
import org.meristem.oneapp.trustiesservice.validations.constraints.ContainsEnum;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(
        name = "ComprehensiveWillRequest",
        description = "Request payload containing comprehensive will information.",
        example = "{\n" +
                "  \"marriageType\": \"ISLAMIC_MARRIAGE\",\n" +
                "  \"religion\": \"CHRISTIANITY\",\n" +
                "  \"occupation\": \"Software Engineer\",\n" +
                "  \"customaryTradition\": \"YES\",\n" +
                "  \"traditionDetails\": \"Follows XYZ tradition\",\n" +
                "  \"otherDetails\": \"Additional notes\"\n" +
                "}"
)
public class CreateComprehensiveWillRequest extends CreateWillRequest {

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
