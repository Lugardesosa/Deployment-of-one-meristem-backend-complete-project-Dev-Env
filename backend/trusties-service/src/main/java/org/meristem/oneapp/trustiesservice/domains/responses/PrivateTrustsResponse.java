package org.meristem.oneapp.trustiesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@SuperBuilder
@Schema(
        description = "Response payload for a Private Trust plan, including trustee powers, beneficiaries, designated representatives, and plan dates.",
        example = """
        {
          "title": "Family Education Trust",
          "objective": "Provide for beneficiaries' education expenses",
          "frequency": "ANNUAL",
          "commencementDate": "2025-01-01",
          "terminationDate": "2045-12-31",
          "powerOfTrustee": ["INVEST_FUNDS", "DISTRIBUTE_INCOME"],
          "beneficiaries": [],
          "designatedRepresentatives": [
            {
              "representativeName": "Alice Smith",
              "representativeAddress": "123 Main St, Springfield",
              "representativeEmail": "alice.smith@example.com",
              "representativePhoneNumber": "+1-555-0100"
            }
          ]
        }
        """
)
public class PrivateTrustsResponse extends PlanResponse {

    private String title;

    private String objective;

    private String frequency;

    private LocalDate commencementDate;

    private LocalDate terminationDate;

    private String[] powerOfTrustee;

    @Builder.Default
    @ArraySchema(
            arraySchema = @Schema(description = "Beneficiaries designated for the plan", example = "[]"),
            schema = @Schema(implementation = PlanBeneficiariesResponse.class),
            uniqueItems = true
    )
    private Set<PlanBeneficiariesResponse> beneficiaries =  new HashSet<>();


    private Set<DesignatedRepresentativeResponse> designatedRepresentatives;


    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class DesignatedRepresentativeResponse {

        private String representativeName;

        private String representativeAddress;

        private String representativeEmail;

        private String representativePhoneNumber;
    }

}
