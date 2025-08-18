package org.meristem.oneapp.trustiesservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.trustiesservice.repositories.CustomRepository;
import org.meristem.oneapp.trustiesservice.validations.constraints.ExistsById;

@RequiredArgsConstructor
public class ExistsByIdValidator implements ConstraintValidator<ExistsById, Long> {

    private final CustomRepository repository;
    private Class<?> tableName;

    @Override
    public void initialize(ExistsById constraintAnnotation) {
        tableName = constraintAnnotation.tableName();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        try {
            return repository.existById(tableName, id);
        } catch (Exception e) {
            return false;
        }
    }
}
