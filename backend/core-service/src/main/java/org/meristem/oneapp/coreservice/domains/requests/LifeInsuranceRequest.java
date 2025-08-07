package org.meristem.oneapp.coreservice.domains.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Request object for life insurance asset")
public class LifeInsuranceRequest extends AssetRequest {

    @Schema(description = "Name of the insurance company", example = "Leadway Assurance")
    @NotBlank(message = "Cannot be blank")
    @Size(min = 1, max = 100)
    private String insuranceCompany;

    @Schema(description = "Policy number of the insurance", example = "POL123456789")
    @Size(min = 1, max = 300)
    @NotBlank(message = "Cannot be blank")
    private String policyNumber;

    @Schema(description = "Expiry date of the insurance policy", example = "2025-12-31")
    @NotNull(message = "Cannot be null")
    private LocalDate expiryDate;
}
