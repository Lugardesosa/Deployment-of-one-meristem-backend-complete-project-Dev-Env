package org.meristem.oneapp.trustiesservice.validations.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.trustiesservice.validations.validators.AllHundredPercentValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllHundredPercentValidator.class)
public @interface AllHundredPercent {

    String message() default "All value does not sum up to hundred percent";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
