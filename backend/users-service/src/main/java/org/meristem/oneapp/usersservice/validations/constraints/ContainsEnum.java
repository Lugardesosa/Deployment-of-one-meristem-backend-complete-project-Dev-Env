package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.ContainsEnumValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContainsEnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContainsEnum {

    String message() default "Pass a valid enum value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> enumClass();
}
