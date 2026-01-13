package org.meristem.oneapp.usersservice.validations.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.meristem.oneapp.usersservice.validations.validators.PinMatchValidator;

import java.lang.annotation.*;

/**
 * Custom Bean Validation annotation used to validate that
 * two PIN (or password-like) fields on a class match.
 *
 * This annotation must be placed on a class (TYPE),
 * not on individual fields.
 */
@Documented // Ensures this annotation appears in generated JavaDocs
@Retention(RetentionPolicy.RUNTIME) // Keeps the annotation available at runtime for validation
@Target(ElementType.TYPE) // Restricts usage to classes, records, or interfaces
@Constraint(validatedBy = PinMatchValidator.class) // Links this annotation to its validator logic
public @interface PinMatch {

    /**
     * Default validation error message returned when the PIN values do not match.
     * Can be overridden at the usage site.
     */
    String message() default "Password does not match";

    /**
     * Validation groups this constraint belongs to.
     * Used to apply this constraint conditionally in different validation contexts.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for clients of the Bean Validation API.
     * Typically unused; included to satisfy the Bean Validation specification.
     */
    Class<? extends Payload>[] payload() default {};
}
