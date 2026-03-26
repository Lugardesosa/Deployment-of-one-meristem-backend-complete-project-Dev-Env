package org.meristem.oneapp.usersservice.domains.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.meristem.oneapp.usersservice.domains.enums.InvestmentInstruments;

public record QueryExistingUserRequest(@Schema(pattern = "^[0-9]{11}$", example = "12345678901") @Pattern(regexp = "^[0-9]{11}$", message = "Pass a valid bvn") @NotBlank(message = "Pass a valid bvn") String idNumber, @NotNull(message = "Cannot be null") InvestmentInstruments investmentInstruments) {
}
