package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.DependentRelationship;
import org.meristem.oneapp.usersservice.validations.constraints.Name;

import java.time.LocalDate;

@Builder
public record CreateUserDependentRequest(
        @Schema(example = "John", description = "Dependent's first name") @Name(message = "alphabets allowed") @NotBlank(message = "cannot be blank") @Size(min = 1, max = 150, message = "cannot be less than 1 and more than 150") String firstName,
        @Schema(example = "Doe", description = "Dependent's last name") @Name(message = "alphabets allowed") @NotBlank(message = "cannot be blank") @Size(min = 1, max = 150, message = "cannot be less than 1 and more than 150") String lastName,
        @Schema(example = "Obus", description = "Dependent's middle name") @Name(message = "alphabets allowed") @Size(max = 150, message = "cannot be more than 150") String middleName,
        @Schema(example = "07-08-2008", description = "Dependent's dob") @NotNull(message = "Cannot be null") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate dob,
        @Schema(example = "SON", description = "Dependent's relationship to you") @NotNull(message = "Cannot be null") DependentRelationship relationship,
        @Schema(pattern = "^[0-9]{11}$", example = "12345678901") @Pattern(regexp = "^[0-9]{11}$", message = "Pass a valid nin") @NotBlank(message = "Pass a valid nin") String nin
) {
}
