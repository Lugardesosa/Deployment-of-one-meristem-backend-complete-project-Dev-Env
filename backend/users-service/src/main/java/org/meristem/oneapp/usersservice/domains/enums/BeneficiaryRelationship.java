package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BeneficiaryRelationship {

    CHILD("Child"),
    SPOUSE("Spouse"),
    DEPENDENT("Dependent");

    private final String value;

    public static BeneficiaryRelationship fromValue(String value) {
        return switch (value) {
            case "Child", "CHILD" -> CHILD;
            case "Spouse", "SPOUSE" -> SPOUSE;
            case "Dependent", "DEPENDENT" -> DEPENDENT;
            default -> null;
        };
    }
}
