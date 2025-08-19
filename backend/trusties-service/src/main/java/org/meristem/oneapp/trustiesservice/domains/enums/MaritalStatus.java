package org.meristem.oneapp.trustiesservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaritalStatus {

    SINGLE("Single"),
    DIVORCED("Divorced"),
    WIDOWED("Widowed"),
    MARRIED("Married"),;

    private final String value;

    public static MaritalStatus fromValue(String value) {
        return switch (value) {
            case "Single" -> SINGLE;
            case "Divorced" -> DIVORCED;
            case "Widowed" -> WIDOWED;
            case "Married" -> MARRIED;
            default -> null;
        };
    }
}
