package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DobRequest(@Schema(examples = {"2000-09-20", "1990-01-10"}, description = "Pass the user's dob") @NotNull(message = "Cannot be null")
                         LocalDate dob, @Schema(examples = {"20", "1"}, description = "Pass the users id") @NotNull(message = "Cannot be null") Long userId) {
}
