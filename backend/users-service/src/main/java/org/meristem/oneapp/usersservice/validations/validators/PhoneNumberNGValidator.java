package org.meristem.oneapp.usersservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.validations.constraints.PhoneNumberNG;

import static java.util.Objects.isNull;

public class PhoneNumberNGValidator implements ConstraintValidator<PhoneNumberNG, String> {


    @Override
    public void initialize(PhoneNumberNG constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isNull(value)) {
            return true;
        }
        return value.matches(AppConstants.PHONE_NG_REGEX_PATTERN);
    }
}
