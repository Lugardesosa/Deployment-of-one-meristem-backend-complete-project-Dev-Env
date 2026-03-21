package org.meristem.oneapp.trusteesservice.domains.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Request object for pension asset")
public class PensionRequest extends AssetRequest {

    @Schema(description = "Name of the Pension Fund Administrator (PFA)", example = "Stanbic IBTC")
    @NotBlank(message = "Cannot be blank")
    @Size(min = 1, max = 150)
    private String pfa;

    @Schema(description = "Retirement Savings Account (RSA) number", example = "RSA123456789")
    @Size(min = 1, max = 150)
    @NotBlank(message = "Cannot be blank")
    private String rsa;

}
