package org.meristem.oneapp.trustiesservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.trustiesservice.validations.constraints.PastDate;

import java.time.LocalDate;

import static java.util.Objects.isNull;

public class PastDateValidator implements ConstraintValidator<PastDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (isNull(value)) {
            return true;
        }
        return LocalDate.now().minusDays(1L).isBefore(value);
    }
}
