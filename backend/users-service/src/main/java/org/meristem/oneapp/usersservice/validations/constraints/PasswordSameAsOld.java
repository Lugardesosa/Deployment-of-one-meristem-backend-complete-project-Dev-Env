package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.PasswordSameAsOldValidator;

import java.lang.annotation.*;

/**
 * Custom validation constraint annotation that ensures a new password is not the same as the old password.
 * <p>
 * This annotation is used at the class/type level to validate password change operations where both
 * the old and new password values are present in the same object.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * &#64;PasswordSameAsOld
 * public class ChangePasswordRequest {
 *     private String oldPassword;
 *     private String newPassword;
 *     // getters and setters
 * }
 * </pre>
 *
 * <p>
 * The validation is performed by {@link PasswordSameAsOldValidator}, which compares the old and new
 * password values to ensure they are different.
 * </p>
 *
 * @see PasswordSameAsOldValidator
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordSameAsOldValidator.class)
public @interface PasswordSameAsOld {

    /**
     * The error message to be returned when validation fails.
     *
     * @return the error message template
     */
    String message() default "Password cannot be the same as old";

    /**
     * Allows specification of validation groups to which this constraint belongs.
     *
     * @return the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Jakarta Validation API to assign custom payload objects to a constraint.
     *
     * @return the payload objects
     */
    Class<? extends Payload>[] payload() default {};
}
