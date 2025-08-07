package org.meristem.oneapp.coreservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class PensionResponse extends AssetResponse {

    @Schema(description = "Name of the Pension Fund Administrator (PFA)", example = "Stanbic IBTC")
    private String pfa;

    @Schema(description = "Retirement Savings Account (RSA) number", example = "RSA123456789")
    private String rsa;
}
