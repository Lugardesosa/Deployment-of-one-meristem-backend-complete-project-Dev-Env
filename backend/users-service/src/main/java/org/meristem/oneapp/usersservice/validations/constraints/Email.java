package org.meristem.oneapp.usersservice.validations.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.EmailValidator;

import java.lang.annotation.*;


/**
 * Custom validation annotation for validating email addresses.
 * <p>
 * This annotation can be applied to fields or method parameters to ensure that the annotated
 * element contains a valid email address format. The validation logic is implemented in
 * {@link EmailValidator}.
 * </p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>
 * public class User {
 *     {@literal @}Email
 *     private String emailAddress;
 * }
 * </pre>
 *
 * @see EmailValidator
 * @since 1.0
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    /**
     * The error message to be returned when validation fails.
     * <p>
     * This message will be used to create a constraint violation when the annotated
     * element does not contain a valid email address.
     * </p>
     *
     * @return the error message template
     */
    String message() default "recipient is not valid";

    /**
     * Allows specification of validation groups to which this constraint belongs.
     * <p>
     * This attribute is used to group constraints for partial validation scenarios.
     * By default, the constraint belongs to the {@code Default} group.
     * </p>
     *
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Bean Validation API to assign custom payload objects
     * to a constraint.
     * <p>
     * This attribute is typically used to attach metadata information to constraints,
     * such as severity levels or error codes.
     * </p>
     *
     * @return the payload associated with the constraint
     */
    Class<? extends Payload>[] payload() default {};
}
