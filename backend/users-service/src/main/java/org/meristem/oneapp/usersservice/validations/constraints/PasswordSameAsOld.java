package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.PasswordSameAsOldValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordSameAsOldValidator.class)
public @interface PasswordSameAsOld {

    String message() default "Password cannot be the same as old";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
