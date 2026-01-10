package org.meristem.oneapp.usersservice.validations.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.ExistsByIdValidator;

import java.lang.annotation.*;


/**
 * Custom validation constraint annotation to verify that an entity with the specified ID exists in the database.
 * <p>
 * This annotation can be applied to fields or method parameters to validate that the provided ID value
 * corresponds to an existing record in the database table associated with the specified entity class.
 * </p>
 * <p>
 * The validation is performed by {@link ExistsByIdValidator}, which checks the database for the existence
 * of an entity with the given ID.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * public class AssignRoleRequest {
 *     @ExistsById(tableName = Role.class, message = "Role does not exist")
 *     private Long roleId;
 * }
 * }</pre>
 *
 * @see ExistsByIdValidator
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ExistsByIdValidator.class)
public @interface ExistsById {
    /**
     * The error message to be returned when validation fails.
     * <p>
     * This message will be included in the validation error response when the entity with the specified ID
     * does not exist in the database.
     * </p>
     *
     * @return the validation error message
     */
    String message() default "item does not exist.";

    /**
     * The entity class that represents the database table to check for existence.
     * <p>
     * This should be the JPA entity class whose table will be queried to verify the existence
     * of a record with the provided ID.
     * </p>
     *
     * @return the entity class to validate against
     */
    Class<?> tableName();

    /**
     * Allows specification of validation groups to which this constraint belongs.
     * <p>
     * This is a standard Bean Validation feature that allows grouping of constraints for selective validation.
     * </p>
     *
     * @return the validation groups this constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Bean Validation API to assign custom payload objects to a constraint.
     * <p>
     * This is typically not used by application code but can be leveraged by validation frameworks
     * for additional metadata.
     * </p>
     *
     * @return the payload objects associated with this constraint
     */
    Class<? extends Payload>[] payload() default {};
}
