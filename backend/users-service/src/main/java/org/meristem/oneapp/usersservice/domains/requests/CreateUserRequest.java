package org.meristem.oneapp.usersservice.domains.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.meristem.oneapp.usersservice.validations.constraints.Email;
import org.meristem.oneapp.usersservice.validations.constraints.Name;
import org.meristem.oneapp.usersservice.validations.constraints.PhoneNumberNG;

import static java.util.Objects.isNull;

@Builder
public record CreateUserRequest(@Schema(example = "johndoe@gmail.com", description = "Pass the users email") @Email @NotBlank(message = "cannot be null") @Size(min = 5, max = 200, message = "cannot be longer than 200 and less than 5") String email,
                                @Schema(example = "John", description = "Users first name") @Name(message = "alphabets allowed") @NotBlank(message = "cannot be blank") @Size(min = 1, max = 150, message = "cannot be less than 1 and more than 150") String firstName,
                                @Schema(example = "Doe", description = "Users last name") @Name(message = "alphabets allowed") @NotBlank(message = "cannot be blank") @Size(min = 1, max = 150, message = "cannot be less than 1 and more than 150") String lastName,
                                @Schema(example = "Obus", description = "Users middle name") @Name(message = "alphabets allowed") @Size(max = 150, message = "cannot be more than 150") String middleName,
                                @Schema(example = "+2349098989876", description = "Pass the user's phone number. can be 08123456545, 2348123456545, +2348123456545") @NotBlank(message = "cannot be null") @PhoneNumberNG @Size(min = 11, max = 14, message = "cannot be less than 1 and more than 14") String phoneNumber,
                                @Schema(example = "MW-ABISOLAZ52", description = "Enter the user referral code of the user that referred you.") @Pattern(regexp = "^MW-\\w{0,14}$") String referralCode) {
    @Override
    public String phoneNumber() {
        return phoneNumber.replace("+", "").replaceAll("^234", "0");
    }

    @Override
    public String firstName() {
        return firstName.trim();
    }

    @Override
    public String lastName() {
        return lastName.trim();
    }

    public String middleName() {
        return isNull(middleName) ? null : middleName.trim();
    }
}