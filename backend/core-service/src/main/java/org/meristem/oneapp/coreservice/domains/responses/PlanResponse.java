package org.meristem.oneapp.coreservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Response payload for a plan, including owner information, contact details, and related assets and beneficiaries.")
public class PlanResponse {

    @Schema(description = "Unique identifier of the plan", example = "101")
    private Long id;

    @Schema(description = "Owner's last name", example = "Doe")
    private String lastName;

    @Schema(description = "Owner's first name", example = "Jane")
    private String firstName;

    @Schema(description = "Owner's middle name or initial", example = "A.")
    private String middleName;

    @Schema(description = "Email address of the plan owner", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "Contact phone number in E.164 or local format", example = "+1-555-123-4567")
    private String phoneNumber;

    @Schema(description = "Mailing address of the plan owner", example = "123 Main St, Springfield, IL 62704")
    private String address;

    @Schema(description = "Identifier of the plan owner", example = "42")
    private Long ownerId;
}
