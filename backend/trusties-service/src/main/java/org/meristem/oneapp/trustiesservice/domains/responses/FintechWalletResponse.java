package org.meristem.oneapp.trustiesservice.domains.responses;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
public class FintechWalletResponse extends AssetResponse {

    @Schema(description = "Name of the fintech application", example = "Flutterwave")
    @NotNull(message = "Cannot be null")
    private String fintechApp;

    @Schema(description = "Unique identifier for the wallet", example = "abc123xyz")
    @NotNull(message = "Cannot be null")
    private String uniqueId;
}
