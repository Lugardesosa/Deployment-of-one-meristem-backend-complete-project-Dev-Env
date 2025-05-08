package org.meristem.oneapp.usersservice.validations.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.PasswordMatchValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {

    String message() default "Password does not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
