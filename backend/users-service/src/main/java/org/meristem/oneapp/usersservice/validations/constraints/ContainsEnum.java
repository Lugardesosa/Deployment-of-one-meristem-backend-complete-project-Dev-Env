package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.ContainsEnumValidator;

import java.lang.annotation.*;

/**
 * Validates that a field or parameter value matches one of the constants in a specified enum class.
 * This annotation is used to ensure that string or other values correspond to valid enum constants.
 *
 * <p>Example usage:</p>
 * <pre>
 * public class UserRequest {
 *     &#64;ContainsEnum(enumClass = UserRole.class, message = "Invalid user role")
 *     private String role;
 * }
 * </pre>
 *
 * <p>The validation is performed by {@link ContainsEnumValidator}.</p>
 *
 * @see ContainsEnumValidator
 * @since 1.0
 */
@Documented
@Constraint(validatedBy = ContainsEnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContainsEnum {

    /**
     * Returns the error message to be used when validation fails.
     *
     * @return the error message template
     */
    String message() default "Pass a valid enum value";

    /**
     * Returns the validation groups to which this constraint belongs.
     *
     * @return the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Returns the payload associated with this constraint.
     *
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Specifies the enum class against which the value should be validated.
     * The annotated field's value must match one of the constants defined in this enum class.
     *
     * @return the enum class to validate against
     */
    Class<? extends Enum<?>> enumClass();
}
