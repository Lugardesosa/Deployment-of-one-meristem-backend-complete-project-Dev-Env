package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaritalStatus {

    SINGLE("Single", 1),
    DIVORCED("Divorced", 2),
    WIDOWED("Widowed", 3),
    MARRIED("Married", 4);

    private final String value;
    private final Integer number;

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
