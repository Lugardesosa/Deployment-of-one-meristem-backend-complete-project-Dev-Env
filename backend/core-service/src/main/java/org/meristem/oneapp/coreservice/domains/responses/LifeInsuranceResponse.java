package org.meristem.oneapp.coreservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Response object for life insurance asset")
public class LifeInsuranceResponse extends AssetResponse {

    @Schema(description = "Name of the insurance company", example = "Leadway Assurance")
    private String insuranceCompany;

    @Schema(description = "Policy number of the insurance", example = "POL123456789")
    private String policyNumber;

    @Schema(description = "Expiry date of the insurance policy", example = "2025-12-31")
    private LocalDate expiryDate;
}
