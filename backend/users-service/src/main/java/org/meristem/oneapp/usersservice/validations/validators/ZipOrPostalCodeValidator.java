package org.meristem.oneapp.usersservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.usersservice.domains.requests.AddressVerificationRequest;
import org.meristem.oneapp.usersservice.validations.constraints.ZipOrPostalCode;

import static java.util.Objects.nonNull;

public class ZipOrPostalCodeValidator implements ConstraintValidator<ZipOrPostalCode, AddressVerificationRequest> {

    @Override
    public boolean isValid(AddressVerificationRequest value, ConstraintValidatorContext context) {
        if (!value.liveInNigeria()) {
            return nonNull(value.zipOrPostalCode());
        }
        return true;
    }
}
