package org.meristem.oneapp.trustiesservice.domains.responses;

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
@Schema(description = "Response object for alternate asset")
public class AlternateAssetsResponse extends AssetResponse {

    @Schema(description = "Type of alternate asset", example = "Crypto")
    private String assetType;

    @Schema(description = "Platform name", example = "Binance")
    private String platform;

    @Schema(description = "Unique identifier for the asset", example = "user123")
    private String uniqueId;

    @Schema(description = "Wallet address for the asset", example = "0x123abc456def")
    private String walletAddress;
}
