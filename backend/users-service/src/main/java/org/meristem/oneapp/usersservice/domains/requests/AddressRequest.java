package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @Schema(example = "12 Downtown Avenue, Spasm Road", description = "Pass the user's home address, example '12 Downtown Avenue, Spasm Road'") @NotBlank(message = "Cannot be blank") @Size(max = 150, message = "Not more than 150 chars") String houseAddress,
        @Schema(example = "Yaba", description = "Pass the user's city") @NotBlank(message = "Cannot be blank") @Size(max = 100, message = "Not more than 100 chars") String city,
        @Schema(example = "Unilag", description = "Enter your nearest landmark") @NotBlank(message = "Cannot be blank") @Size(max = 100, message = "Not more than 100 chars") String landmark) {

}
