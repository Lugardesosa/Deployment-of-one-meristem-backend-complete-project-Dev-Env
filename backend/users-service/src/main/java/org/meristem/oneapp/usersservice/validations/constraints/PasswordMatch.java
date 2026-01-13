package org.meristem.oneapp.usersservice.validations.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.PasswordMatchValidator;

import java.lang.annotation.*;

/**
 * Custom validation annotation to ensure that password fields match within a class.
 * This annotation is applied at the class/type level to validate that password and
 * password confirmation fields contain the same value.
 * <p>
 * The actual validation logic is implemented in {@link PasswordMatchValidator}.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * {@code
 * @PasswordMatch
 * public class RegistrationRequest {
 *     private String password;
 *     private String confirmPassword;
 *     // getters and setters
 * }
 * }
 * </pre>
 *
 * @see PasswordMatchValidator
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {

    /**
     * The error message to be returned when validation fails.
     *
     * @return the error message template, defaults to "Passwords do not match"
     */
    String message() default "Passwords do not match";

    /**
     * Allows specification of validation groups, to which this constraint belongs.
     * This is used to perform partial validation.
     *
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Bean Validation API to assign custom payload objects
     * to a constraint. This attribute is not used by the API itself.
     *
     * @return the payload associated with the constraint
     */
    Class<? extends Payload>[] payload() default {};
}
