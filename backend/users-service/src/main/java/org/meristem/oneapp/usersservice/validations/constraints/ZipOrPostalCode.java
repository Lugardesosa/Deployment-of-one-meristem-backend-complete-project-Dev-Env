package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.ZipOrPostalCodeValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = ZipOrPostalCodeValidator.class)
public @interface ZipOrPostalCode {

    String message() default "Pass zip or postal code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
