package org.meristem.oneapp.trusteesservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.meristem.oneapp.trusteesservice.validations.constraints.AllHundredPercent;
import org.meristem.oneapp.trusteesservice.validations.constraints.Email;

import java.util.List;

@Builder
@Schema(
        name = "CreateNominatedFundRequest",
        description = "Request to create a nominated fund for a beneficiary.",
        example = """
        {
          "lastName": "Doe",
          "firstName": "Jane",
          "email": "jane.doe@example.com",
          "phoneNumber": "+2348012345678",
          "address": "123 Main Street, Lagos",
          "beneficiaryInformation": {
            "beneficiaryId": 987654321,
            "percentage": 50.0
          }
        }
        """
)
public record CreateNominatedFundRequest(

        @Schema(description = "Beneficiary's last name", example = "Doe")
        @NotBlank(message = "Cannot be blank")
        String lastName,

        @Schema(description = "Beneficiary's first name", example = "Jane")
        @NotBlank(message = "Cannot be blank")
        String firstName,

        @Schema(description = "Contact email address", format = "email", example = "jane.doe@example.com")
        @Email(message = "Enter a valid email")
        @NotBlank(message = "Cannot be blank")
        String email,

        @Schema(description = "Contact phone number in international format", example = "+2348012345678")
        @NotBlank(message = "Cannot be blank")
        String phoneNumber,

        @Schema(description = "Residential address", example = "123 Main Street, Lagos")
        @NotBlank(message = "Cannot be blank")
        String address,

        @Schema(description = "Information about the nominated beneficiary")
        @NotEmpty(message = "Cannot be empty")
        @AllHundredPercent
        List<BeneficiaryInformation> beneficiaryInformation

        ) {
    @Schema(
            name = "BeneficiaryInformation",
            description = "Details of the nominated beneficiary.",
            example = """
            {
              "beneficiaryId": 987654321,
              "percentage": 50.0
            }
            """
    )
    public record BeneficiaryInformation(
            @Schema(description = "Unique identifier of the beneficiary", example = "987654321")
            @NotNull(message = "Cannot be null")
            Long beneficiaryId,

            @Schema(description = "Allocation amount or percentage for the beneficiary", example = "50.0")
            @NotNull(message = "Cannot be null")
            Double percentage
    ) {

    }
}
