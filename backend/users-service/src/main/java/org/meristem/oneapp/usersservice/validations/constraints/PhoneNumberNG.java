package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.PhoneNumberNGValidator;

import java.lang.annotation.*;

/**
 * Custom validation annotation for validating Nigerian phone numbers.
 * <p>
 * This annotation can be applied to fields or method parameters to ensure that
 * the value represents a valid Nigerian phone number format.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * public class UserRequest {
 *     &#64;PhoneNumberNG
 *     private String phoneNumber;
 * }
 * </pre>
 * </p>
 *
 * @see PhoneNumberNGValidator
 */
@Documented
@Constraint(validatedBy = PhoneNumberNGValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberNG {
    /**
     * The error message to be returned when validation fails.
     *
     * @return the error message template
     */
    String message() default "phone number is not valid";

    /**
     * Allows the specification of validation groups to which this constraint belongs.
     *
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Bean Validation API to assign custom payload objects to a constraint.
     *
     * @return the payload associated with the constraint
     */
    Class<? extends Payload>[] payload() default {};
}
