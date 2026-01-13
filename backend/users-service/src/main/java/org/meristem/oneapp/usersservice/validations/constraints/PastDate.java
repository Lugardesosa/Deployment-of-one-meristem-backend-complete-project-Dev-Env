package org.meristem.oneapp.usersservice.validations.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.PastDateValidator;

import java.lang.annotation.*;

/**
 * Validation constraint annotation that ensures a date value is in the past.
 * <p>
 * This annotation can be applied to fields or method parameters to validate that the
 * date/time value is before the current date and time. The validation is performed
 * by {@link PastDateValidator}.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 * public class UserProfile {
 *     &#64;PastDate
 *     private LocalDate birthDate;
 *
 *     &#64;PastDate(message = "Registration date must be in the past")
 *     private LocalDateTime registrationDate;
 * }
 * </pre>
 *
 * @see PastDateValidator
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = PastDateValidator.class)
public @interface PastDate {

    /**
     * The error message to be returned when validation fails.
     * <p>
     * Default message: "must be in the past"
     * </p>
     *
     * @return the error message template
     */
    String message() default "must be in the past";

    /**
     * Allows specification of validation groups to which this constraint belongs.
     * <p>
     * This is used to define different validation scenarios where this constraint
     * may or may not apply.
     * </p>
     *
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default { };

    /**
     * Can be used by clients of the Bean Validation API to assign custom payload objects
     * to a constraint. This attribute is not used by the API itself.
     *
     * @return the payload associated with the constraint
     */
    Class<? extends Payload>[] payload() default { };
}
