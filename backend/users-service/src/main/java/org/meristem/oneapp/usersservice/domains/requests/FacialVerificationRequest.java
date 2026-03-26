package org.meristem.oneapp.usersservice.domains.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record FacialVerificationRequest(
        @Schema(pattern = "^[0-9]{11}$", example = "12345678901") @Pattern(regexp = "^[0-9]{11}$", message = "Pass a valid bvn") @NotBlank(message = "Pass a valid bvn") String idNumber,
        @Pattern(regexp = "^BVN|NIN_V2$", message = "Pass a valid idNumber type (BVN or NIN_V2)") @NotBlank(message = "Pass a valid type") String idType
) {
}
