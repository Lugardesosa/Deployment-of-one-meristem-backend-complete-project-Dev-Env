package org.meristem.oneapp.usersservice.validations.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.usersservice.domains.requests.PinRequest;
import org.meristem.oneapp.usersservice.validations.constraints.PinMatch;

import static java.util.Objects.isNull;

public class PinMatchValidator implements ConstraintValidator<PinMatch, Object> {

    @Override
    public void initialize(PinMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        PinRequest request = (PinRequest) value;
        if (isNull(request.pin()) || isNull(request.confirmPin())) {
            return true;
        }
        return request.pin().equals(request.confirmPin());
    }
}
