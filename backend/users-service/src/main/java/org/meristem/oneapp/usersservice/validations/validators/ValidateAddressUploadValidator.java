package org.meristem.oneapp.usersservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.usersservice.domains.enums.UtilityBillType;
import org.meristem.oneapp.usersservice.domains.requests.AddressVerificationRequest;
import org.meristem.oneapp.usersservice.validations.constraints.ValidateAddressUpload;

public class ValidateAddressUploadValidator implements ConstraintValidator<ValidateAddressUpload, AddressVerificationRequest> {

    @Override
    public boolean isValid(AddressVerificationRequest value, ConstraintValidatorContext context) {
        return (!value.liveInNigeria() || !UtilityBillType.NOTARISED_DOCUMENT.equals(value.utilityBillType())) &&
                (value.liveInNigeria() || UtilityBillType.NOTARISED_DOCUMENT.equals(value.utilityBillType()));
    }
}
