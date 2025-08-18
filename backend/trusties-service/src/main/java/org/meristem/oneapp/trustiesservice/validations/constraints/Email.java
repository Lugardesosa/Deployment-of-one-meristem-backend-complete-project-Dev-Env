package org.meristem.oneapp.trustiesservice.validations.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.trustiesservice.validations.validators.EmailValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default "recipient is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
