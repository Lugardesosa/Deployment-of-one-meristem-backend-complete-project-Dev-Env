package org.meristem.oneapp.walletservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddAccountRequest(

        @Schema(pattern = "^[0-9]{10}$", example = "1234567890") @Pattern(regexp = "^[0-9]{10}$", message = "Pass a valid bank account number") @NotBlank(message = "Pass a valid accountNumber") String accountNumber,
        @NotBlank(message = "Pass a valid bank code") String bankCode
) {
}
