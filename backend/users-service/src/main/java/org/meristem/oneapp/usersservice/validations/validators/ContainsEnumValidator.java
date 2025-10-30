package org.meristem.oneapp.usersservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.usersservice.validations.constraints.ContainsEnum;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContainsEnumValidator implements ConstraintValidator<ContainsEnum, CharSequence> {

    private List<String> acceptedValues;
    @Override
    public void initialize(ContainsEnum constraintAnnotation) {
        acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return acceptedValues.contains(value.toString());
    }
}
