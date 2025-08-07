package org.meristem.oneapp.coreservice.domains.requests;

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
@Schema(description = "Request object for alternate asset")
public class AlternateAssetsRequest extends AssetRequest {

    @Schema(description = "Type of alternate asset", example = "Crypto")
    @NotBlank(message = "Cannot be blank")
    private String assetType;

    @Schema(description = "Platform name", example = "Binance")
    @NotBlank(message = "Cannot be blank")
    @Size(max = 150, message = "Must not be more than 150")
    private String platform;

    @Schema(description = "Unique identifier for the asset", example = "user123")
    @Size(max = 300, message = "Must not be more than 300")
    @NotBlank(message = "Cannot be blank")
    private String uniqueId;

    @Schema(description = "Wallet address for the asset", example = "0x123abc456def")
    private String walletAddress;
}
