package org.meristem.oneapp.trusteesservice.validations.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.trusteesservice.validations.validators.PastDateValidator;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = PastDateValidator.class)
public @interface PastDate {

    String message() default "must be in the past";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
