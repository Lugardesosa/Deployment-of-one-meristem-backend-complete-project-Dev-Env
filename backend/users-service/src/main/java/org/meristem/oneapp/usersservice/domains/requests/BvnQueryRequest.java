package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BvnQueryRequest(@Schema(pattern = "^[0-9]{11}$", example = "12345678901") @Pattern(regexp = "^[0-9]{11}$", message = "Pass a valid bvn") @NotBlank(message = "Pass a valid bvn") String bvn, @Pattern(regexp = "^BVN|NIN_V2$", message = "Pass a valid id type (BVN or NIN_V2)") String idType,
                              @NotBlank(message = "Pass a valid country code") String country) {
}
