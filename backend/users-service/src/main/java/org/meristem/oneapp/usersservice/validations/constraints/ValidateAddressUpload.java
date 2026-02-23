package org.meristem.oneapp.usersservice.validations.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.ValidateAddressUploadValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = ValidateAddressUploadValidator.class)
public @interface ValidateAddressUpload {

    String message() default "passed data is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
