package org.meristem.oneapp.usersservice.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.repositories.GeneralRepository;
import org.meristem.oneapp.usersservice.validations.constraints.ExistsById;

@RequiredArgsConstructor
public class ExistsByIdValidator implements ConstraintValidator<ExistsById, String> {

    private final GeneralRepository repository;
    private Class<?> tableName;

    @Override
    public void initialize(ExistsById constraintAnnotation) {
        tableName = constraintAnnotation.tableName();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        try {
            return repository.existById(tableName, Long.valueOf(id));
        } catch (Exception e) {
            return false;
        }
    }
}
