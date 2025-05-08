package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.meristem.oneapp.usersservice.domains.enums.IdCardType;
import org.meristem.oneapp.usersservice.validations.constraints.ContainsEnum;
import org.meristem.oneapp.usersservice.validations.constraints.PastDate;

import java.time.LocalDate;

public record IdRequest(@Schema(anyOf = {IdCardType.class}, example = "NIN", description = "Pass a valid enum, NIN for example") @ContainsEnum(enumClass = IdCardType.class) @NotBlank(message = "Cannot be null") String idCardType,
                        @Schema(example = "12345678912", description = "Pass the users id details") @NotBlank(message = "cannot be null") @Size(min = 9, max = 20, message = "cannot be longer than 20 and less than 9") String value,
                        @Schema(example = "01-05-2020", description = "Pass the card's issued date") @NotNull(message = "cannot be null") @Past(message = "must be in the past") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate issuedDate,
                        @Schema(example = "01-05-2025", description = "Pass the card's expiry date") @PastDate() @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate expiryDate) {
}
