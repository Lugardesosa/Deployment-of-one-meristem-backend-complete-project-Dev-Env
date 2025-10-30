package org.meristem.oneapp.usersservice.validations.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.usersservice.domains.requests.PasswordResetRequest;
import org.meristem.oneapp.usersservice.domains.requests.SetPasswordRequest;
import org.meristem.oneapp.usersservice.validations.constraints.PasswordMatch;

import static java.util.Objects.isNull;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String password, confirmPassword;
        if (value instanceof PasswordResetRequest request) {
            password = request.password();
            confirmPassword = request.confirmPassword();
        } else if (value instanceof SetPasswordRequest request) {
            password = request.password();
            confirmPassword = request.confirmPassword();
        } else {
            return true;
        }
        if (isNull(password) || isNull(confirmPassword)) {
            return true;
        }
        return password.equals(confirmPassword);
    }
}
