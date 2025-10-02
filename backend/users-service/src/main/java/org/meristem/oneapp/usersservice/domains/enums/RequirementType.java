package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequirementType {

    DEFAULT(1, "DEFAULT"),
    MUTUAL_FUNDS(2, "MUTUAL_FUNDS"),
    TREASURY_BILLS(3, "TREASURY_BILLS"),
    TRUSTIES(4, "TRUSTIES");

    private final int id;
    private final String name;

    public static RequirementType fromId(int id) {
        return switch (id) {
            case 1 -> RequirementType.DEFAULT;
            case 2 -> RequirementType.MUTUAL_FUNDS;
            case 3 -> RequirementType.TREASURY_BILLS;
            case 4 -> RequirementType.TRUSTIES;
            default -> null;
        };
    }

    public static RequirementType fromName(String name) {
        return switch(name) {
            case "DEFAULT" -> RequirementType.DEFAULT;
            case "MUTUAL_FUNDS" -> RequirementType.MUTUAL_FUNDS;
            case "TREASURY_BILLS" -> RequirementType.TREASURY_BILLS;
            case "TRUSTEES" -> RequirementType.TRUSTIES;
            default -> null;
        };
    }
}
