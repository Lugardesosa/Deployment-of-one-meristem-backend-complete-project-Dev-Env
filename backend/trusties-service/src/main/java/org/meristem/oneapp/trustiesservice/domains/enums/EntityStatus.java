package org.meristem.oneapp.trustiesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the status of an entity.
 * It can either be ACTIVE or INACTIVE, with corresponding integer values.
 */
@AllArgsConstructor
@Getter
public enum EntityStatus {

    /**
     * Represents an <strong>inactive/false/not approved</strong> status with a value of 0.
     */
    INACTIVE(0),

    /**
     * Represents an <strong>active/true/approved</strong> status with a value of 1.
     */
    ACTIVE(1),

    /**
     * Represents a <strong>partially active/pending/partially saved</strong> status with a value of 2.
     */
    PENDING(2);

    /**
     * The integer value associated with the status.
     */
    private final Integer value;
}
