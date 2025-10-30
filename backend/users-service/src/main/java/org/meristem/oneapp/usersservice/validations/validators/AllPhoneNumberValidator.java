package org.meristem.oneapp.usersservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.validations.constraints.AllPhoneNumber;

import java.util.List;

import static java.util.Objects.isNull;

public class AllPhoneNumberValidator implements ConstraintValidator<AllPhoneNumber, String> {

    // Add all the countries phone number validator regex here
    List<String> allPhoneRegex = List.of(AppConstants.PHONE_NG_REGEX_PATTERN);

    @Override
    public void initialize(AllPhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isNull(value)) {
            return true;
        }
        return allPhoneRegex.stream().anyMatch(value::matches);
    }
}
