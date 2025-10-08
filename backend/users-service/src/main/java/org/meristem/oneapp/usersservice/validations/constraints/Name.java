package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.NameValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    String message() default "name is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
