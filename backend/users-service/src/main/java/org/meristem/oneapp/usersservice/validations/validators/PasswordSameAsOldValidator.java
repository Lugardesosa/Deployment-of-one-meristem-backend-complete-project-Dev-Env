package org.meristem.oneapp.usersservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.usersservice.domains.requests.UpdatePasswordRequest;
import org.meristem.oneapp.usersservice.validations.constraints.PasswordSameAsOld;

import static java.util.Objects.isNull;

public class PasswordSameAsOldValidator implements ConstraintValidator<PasswordSameAsOld, Object> {

    @Override
    public void initialize(PasswordSameAsOld constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }
        UpdatePasswordRequest request = (UpdatePasswordRequest) value;
        if (isNull(request.oldPassword()) || isNull(request.newPassword())) {
            return true;
        }
        return !request.oldPassword().equals(request.newPassword());
    }
}
