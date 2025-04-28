package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.NextOfKins;
import org.meristem.oneapp.usersservice.validations.constraints.ContainsEnum;
import org.meristem.oneapp.usersservice.validations.constraints.Email;
import org.meristem.oneapp.usersservice.validations.constraints.PhoneNumberNG;

@Builder
public record CreateNextOfKinRequest(@Schema(example = "johndoe@gmail.com", description = "Pass the users email") @Email @NotBlank(message = "cannot be null") @Size(min = 5, max = 200, message = "cannot be longer than 200 and less than 5") String email,
                                     @Schema(example = "John Doe", description = "Users full name") @Pattern(regexp = "([A-Za-z]+\\s[A-Za-z]+)+", message = "alphabets allowed") @NotBlank(message = "cannot be blank") @Size(min = 1, max = 150, message = "cannot be less than 3 and more than 150") String fullName,
                                     @Schema(example = "+2349098989876", description = "Pass the user's phone number. can be 08123456545, 2348123456545, +2348123456545") @NotBlank(message = "cannot be null") @PhoneNumberNG @Size(min = 11, max = 14, message = "cannot be less than 1 and more than 14") String phoneNumber,
                                     @Schema(anyOf = {NextOfKins.class}, example = "FATHER", description = "Pass a valid enum, FATHER for example") @ContainsEnum(enumClass = NextOfKins.class) @NotNull(message = "Cannot be null") String nextOfKins,
                                     @Schema(example = "Grand Father", description = "Pass this value if OTHER was chosen as relationship type") String relationship) {
    @Override
    public String phoneNumber() {
        return phoneNumber.replace("+", "").replaceAll("^234", "0");
    }

}
