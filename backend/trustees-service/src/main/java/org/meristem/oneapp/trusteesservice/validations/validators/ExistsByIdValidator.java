package org.meristem.oneapp.trusteesservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.trusteesservice.repositories.CustomRepository;
import org.meristem.oneapp.trusteesservice.validations.constraints.ExistsById;

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
