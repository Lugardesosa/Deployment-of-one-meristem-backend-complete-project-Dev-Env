package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BankDetailsQueryRequest(
        @JsonProperty("id_number") @Schema(pattern = "^[0-9]{11}$", example = "12345678901") @Pattern(regexp = "^[0-9]{11}$", message = "Pass a valid bvn") @NotBlank(message = "Pass a valid bvn") String idNumber,
        @JsonProperty("country") @NotBlank(message = "Pass a valid country code") String country,
        @JsonProperty("bank_code") @NotBlank(message = "Pass a valid bank code") String bankCode) {
}
