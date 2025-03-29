package org.meristem.oneapp.usersservice.domains.requests;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.meristem.oneapp.usersservice.validations.constraints.Email;
import org.meristem.oneapp.usersservice.validations.constraints.Name;
import org.meristem.oneapp.usersservice.validations.constraints.Password;
import org.meristem.oneapp.usersservice.validations.constraints.PhoneNumberNG;

import static java.util.Objects.isNull;


@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateUserRequest(@Email @NotBlank(message = "cannot be null") @Size(min = 5, max = 200, message = "cannot be longer than 200 and less than 5") String email,
                                @Password @NotBlank(message = "cannot be null") @Size(min = 8, max = 20, message = "cannot be more than 20 and less than 8") String password,
                                @Name @NotBlank(message = "cannot be blank") @Size(min = 1, max = 150, message = "cannot be less than 1 and more than 150") String firstName,
                                @Name @NotBlank(message = "cannot be blank") @Size(min = 1, max = 150, message = "cannot be less than 1 and more than 150") String lastName,
                                @Name @Size(max = 150, message = "cannot be more than 150") String middleName,
                                @NotBlank(message = "cannot be null") @PhoneNumberNG @Size(min = 11, max = 14, message = "cannot be less than 1 and more than 14") String phoneNumber) {
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