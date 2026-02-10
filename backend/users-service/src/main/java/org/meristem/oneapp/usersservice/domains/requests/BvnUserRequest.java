package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record BvnUserRequest(@NotBlank(message = "Cannot be blank") @Schema(example = "MALE", allowableValues = {"MALE", "FEMALE"}, description = "pass the user's gender from the nin") String gender,
                             @NotNull(message = "Cannot be blank") @Schema(example = "09-09-2000", description = "pass the user's date of birth") @Past(message = "must be in the past") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate dateOfBirth) {
}
