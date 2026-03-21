package org.meristem.oneapp.trusteesservice.domains.requests;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.meristem.oneapp.trusteesservice.domains.enums.ContributionFrequency;
import org.meristem.oneapp.trusteesservice.domains.enums.PrivateTrustObjectives;
import org.meristem.oneapp.trusteesservice.models.Currencies;
import org.meristem.oneapp.trusteesservice.validations.constraints.ExistsById;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(
        name = "CreatePrivateTrustsRequest",
        description = "Request payload to create a private trust, including settlor details, objectives, contribution plan, beneficiaries, currency, and designated representatives.",
        anyOf = {CreatePrivateTrustsRequest.DesignatedRepresentativeRequest.class},
        example = """
                {
                  "ownerId": 1,
                  "lastName": "Doe",
                  "firstName": "Jane",
                  "email": "jane.doe@example.com",
                  "phoneNumber": "+2348012345678",
                  "address": "123 Lagos Ave, Victoria Island, Lagos",
                  "title": "Ms",
                  "objective": "EDUCATION_TRUST",
                  "frequency": "MONTHLY",
                  "commencementDate": "2025-01-01",
                  "terminationDate": "2030-12-31",
                  "powerOfTrustee": ["Hire Advisers", "Reinvest proceeds"],
                  "beneficiaryIds": [101, 102],
                  "currencyId": 1,
                  "fundContribution": 1000.00,
                  "designatedRepresentativeRequests": [
                    {
                      "representativeName": "Samuel Okoro",
                      "representativeAddress": "45 Adeola Odeku St, Lagos",
                      "representativeEmail": "samuel.okoro@example.com",
                      "representativePhoneNumber": "+2348098765432"
                    }
                  ]
                }"""
)
@Builder
public record CreatePrivateTrustsRequest(


        @Schema(description = "Owner's id if created by an admin", example = "1")
        Long ownerId,

        @NotBlank(message = "Not blank")
        @Size(min = 1, max = 150)
        @Schema(description = "Settlor's last name", example = "Doe")
        String lastName,

        @NotBlank(message = "Not blank")
        @Size(min = 1, max = 150)
        @Schema(description = "Settlor's first name", example = "Jane")
        String firstName,

        @NotBlank(message = "Not blank")
        @Size(min = 1, max = 300)
        @Schema(description = "Contact email address of the settlor", example = "jane.doe@example.com")
        String email,

        @NotBlank(message = "Not blank")
        @Size(min = 1, max = 50)
        @Schema(description = "Contact phone number in international format", example = "+2348012345678")
        String phoneNumber,

        @NotBlank(message = "Not blank")
        @Size(min = 1, max = 400)
        @Schema(description = "Residential or mailing address of the settlor", example = "123 Lagos Ave, Victoria Island, Lagos")
        String address,

        @NotBlank(message = "Not blank")
        @Size(min = 1, max = 50)
        @Schema(description = "Preferred title of the settlor", example = "Ms")
        String title,

        @NotNull(message = "Not null")
        @Schema(anyOf = PrivateTrustObjectives.class, description = "Primary objective of creating the private trust", example = "EDUCATION")
        PrivateTrustObjectives objective,

        @NotNull(message = "Not null")
        @Schema(anyOf = ContributionFrequency.class, description = "Frequency of fund contributions to the trust", example = "MONTHLY")
        ContributionFrequency frequency,

        @NotNull(message = "Not null")
        @Schema(description = "Date when contributions commence (ISO-8601)", example = "2025-01-01")
        LocalDate commencementDate,

        @NotNull(message = "Not null")
        @Schema(description = "Date when the trust terminates (ISO-8601)", example = "2030-12-31")
        LocalDate terminationDate,

        @NotEmpty(message = "Cannot be empty")
        @Size(min = 1, max = 50)
        @ArraySchema(
                arraySchema = @Schema(
                        description = "List of specific powers granted to the trustee.",
                        example = "[\"Invest assets\", \"Distribute income\"]"
                ),
                schema = @Schema(implementation = String.class)
        )
        List<@NotBlank String> powerOfTrustee,

        @ArraySchema(
                arraySchema = @Schema(
                        description = "IDs of beneficiaries associated with the trust.",
                        example = "[101,102]"
                ),
                schema = @Schema(implementation = Long.class)
        )
        @NotEmpty(message = "Cannot be empty")
        List<@NotNull Long> beneficiaryIds,

        @NotNull(message = "Currency assetType cannot be blank")
        @Schema(description = "Currency identifier for contributions to the private trust", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @ExistsById(message = "Currency does not exist", tableName = Currencies.class)
        Long currencyId,

        @DecimalMin(value = "0.0", inclusive = false)
        @NotNull(message = "Estimated amount cannot be null")
        @Schema(description = "Initial or recurring contribution amount for the private trust", example = "1000.00", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal fundContribution,

        @NotEmpty(message = "Cannot be empty")
        @ArraySchema(
                arraySchema = @Schema(
                        description = "List of designated representatives authorized to interact on behalf of the settlor.",
                        example = "[{\"representativeName\":\"Samuel Okoro\",\"representativeAddress\":\"45 Adeola Odeku St, Lagos\",\"representativeEmail\":\"samuel.okoro@example.com\",\"representativePhoneNumber\":\"+2348098765432\"}]"
                ),
                schema = @Schema(implementation = DesignatedRepresentativeRequest.class)
        )
        List<@Valid DesignatedRepresentativeRequest> designatedRepresentativeRequests
) {

    @Schema(
            name = "DesignatedRepresentativeRequest",
            description = "Represents a designated individual authorized to act or be contacted regarding the trust.",
            example = """
                   {
                        "representativeName": "Samuel Okoro",
                        "representativeAddress": "45 Adeola Odeku St, Lagos"
                        "representativeEmail": "samuel.okoro@example.com"
                        "representativePhoneNumber": "+2348098765432"
                    }
            """
    )
    public record DesignatedRepresentativeRequest(

            @NotBlank(message = "Cannot be null")
            @Size(min = 1, max = 300)
            @Schema(description = "Full name of the designated representative", example = "Samuel Okoro")
            String representativeName,

            @Size(min = 1, max = 300)
            @NotBlank(message = "Cannot be null")
            @Schema(description = "Postal or residential address of the representative", example = "45 Adeola Odeku St, Lagos")
            String representativeAddress,

            @Size(min = 1, max = 300)
            @NotBlank(message = "Cannot be null")
            @Schema(description = "Email address of the representative", example = "samuel.okoro@example.com")
            String representativeEmail,

            @Size(min = 1, max = 50)
            @NotBlank(message = "Cannot be null")
            @Schema(description = "Phone number of the representative in international format", example = "+2348098765432")
            String representativePhoneNumber
            ) {
    }
}
