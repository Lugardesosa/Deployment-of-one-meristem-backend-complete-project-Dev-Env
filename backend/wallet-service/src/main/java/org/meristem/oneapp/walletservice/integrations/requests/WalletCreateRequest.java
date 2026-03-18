package org.meristem.oneapp.walletservice.integrations.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(name = "WalletFundRequest", description = "Request body for wallet funding.")
public record WalletCreateRequest(
        String customerId,
        String currency
) {
}
