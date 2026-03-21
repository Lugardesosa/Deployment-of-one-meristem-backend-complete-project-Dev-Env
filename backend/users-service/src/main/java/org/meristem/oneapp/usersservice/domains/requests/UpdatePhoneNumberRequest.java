package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.meristem.oneapp.usersservice.validations.constraints.PhoneNumberNG;

public record UpdatePhoneNumberRequest(@Schema(example = "+2349098989876", description = "Pass the user's phone number. can be 08123456545, 2348123456545, +2348123456545") @NotBlank(message = "cannot be null") @PhoneNumberNG @Size(min = 11, max = 14, message = "cannot be less than 1 and more than 14") String phoneNumber) {

    @Override
    public String phoneNumber() {
        return phoneNumber.replace("+", "");
    }
}
