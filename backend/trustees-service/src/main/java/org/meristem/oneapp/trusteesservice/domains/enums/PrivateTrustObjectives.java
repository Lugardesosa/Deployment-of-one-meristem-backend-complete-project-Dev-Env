package org.meristem.oneapp.trusteesservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PrivateTrustObjectives {
    EDUCATION_TRUST(1, "Education Trust"),
    LIVING_TRUST(2, "Living Trust"),
    TESTAMENTARY_TRUST(3, "Testamentary Trust"),
    OTHER(4, "");

    private final int value;
    private final String displayName;

    public static PrivateTrustObjectives fromValue(int value) {
        for (PrivateTrustObjectives obj : PrivateTrustObjectives.values()) {
            if (obj.value == value) {
                return obj;
            }
        }
        return OTHER;
    }
}
