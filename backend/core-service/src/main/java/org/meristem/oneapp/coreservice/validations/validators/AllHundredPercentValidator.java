package org.meristem.oneapp.coreservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.meristem.oneapp.coreservice.domains.requests.CreateNominatedFundRequest;
import org.meristem.oneapp.coreservice.validations.constraints.AllHundredPercent;

import java.util.List;

public class AllHundredPercentValidator implements ConstraintValidator<AllHundredPercent, List<CreateNominatedFundRequest.BeneficiaryInformation>> {
    @Override
    public void initialize(AllHundredPercent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<CreateNominatedFundRequest.BeneficiaryInformation> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return value.stream().mapToDouble(CreateNominatedFundRequest.BeneficiaryInformation::percentage).sum() == 100.0d;
    }
}
