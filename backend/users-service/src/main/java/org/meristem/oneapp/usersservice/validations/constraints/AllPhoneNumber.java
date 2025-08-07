package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.AllPhoneNumberValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllPhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllPhoneNumber {
    String message() default "phone number is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
