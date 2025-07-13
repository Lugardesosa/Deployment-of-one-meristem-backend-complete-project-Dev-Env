package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.ExistsByIdValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ExistsByIdValidator.class)
public @interface ExistsById {
    String message() default "item does not exist.";
    Class<?> tableName();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
