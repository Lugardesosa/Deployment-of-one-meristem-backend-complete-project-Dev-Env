package org.meristem.oneapp.trustiesservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.trustiesservice.constants.AppConstants;
import org.meristem.oneapp.trustiesservice.validations.constraints.Email;

import static java.util.Objects.isNull;

public class EmailValidator implements ConstraintValidator<Email, String> {


    @Override
    public void initialize(Email constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isNull(value)) {
            return true;
        }
        return value.matches(AppConstants.EMAIL_REGEX_PATTERN);
    }
}
