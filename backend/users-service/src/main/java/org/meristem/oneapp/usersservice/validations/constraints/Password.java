package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.PasswordValidator;

import java.lang.annotation.*;

/**
 * Custom validation annotation for password fields.
 * <p>
 * This annotation validates that a password meets the required security criteria
 * as defined by the {@link PasswordValidator} implementation. It can be applied
 * to fields or method parameters of type String.
 * </p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>
 * public class UserRegistration {
 *     &#64;Password
 *     private String password;
 * }
 * </pre>
 *
 * @see PasswordValidator
 * @since 1.0
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    /**
     * The error message to be returned when validation fails.
     * <p>
     * This message can be overridden when applying the annotation or
     * customized via message properties files.
     * </p>
     *
     * @return the error message template
     */
    String message() default "password is not valid";

    /**
     * Allows specification of validation groups to which this constraint belongs.
     * <p>
     * This is part of the Bean Validation API and allows grouping of constraints
     * for partial validation scenarios.
     * </p>
     *
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Bean Validation API to assign custom
     * payload objects to a constraint.
     * <p>
     * This attribute is typically used to carry metadata information
     * for validation clients.
     * </p>
     *
     * @return the payload associated with the constraint
     */
    Class<? extends Payload>[] payload() default {};
}
