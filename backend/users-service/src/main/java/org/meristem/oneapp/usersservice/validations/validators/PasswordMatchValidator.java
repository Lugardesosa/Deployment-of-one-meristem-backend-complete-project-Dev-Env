package org.meristem.oneapp.usersservice.validations.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.usersservice.domains.requests.PasswordResetRequest;
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
        PasswordResetRequest request = (PasswordResetRequest) value;
        if (isNull(request.password()) || isNull(request.confirmPassword())) {
            return true;
        }
        return request.password().equals(request.confirmPassword());
    }
}
