package org.meristem.oneapp.usersservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.validations.constraints.Name;

import static java.util.Objects.isNull;

public class NameValidator implements ConstraintValidator<Name, String> {


    @Override
    public void initialize(Name constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isNull(value)) {
            return true;
        }
        return value.matches(AppConstants.NAME_REGEX);
    }
}
