package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.Gender;

@Builder
public record GenderRequest(@Schema(examples = {"FEMALE", "MALE"}, description = "Pass the user's dob") @NotNull(message = "Cannot be null")
                            Gender gender, @Schema(examples = {"20", "1"}, description = "Pass the users id") @NotNull(message = "Cannot be null") Long userId) {
}
