package org.meristem.oneapp.walletservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddAccountNumberRequest(
        @Schema(example = "044", description = "bank code of the customer's bank") @NotBlank(message = "Cannot be blank") String bankCode,
        @Schema(example = "1234567890", description = "Customer's bank account number") @Pattern(regexp = "^[0-9]{10}$") @NotBlank(message = "Cannot be blank") String accountNumber) {
}
