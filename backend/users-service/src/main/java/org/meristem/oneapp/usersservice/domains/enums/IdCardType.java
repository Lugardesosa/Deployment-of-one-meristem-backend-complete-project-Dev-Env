package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdCardType {

    BVN("BVN", "Bvn"),
    NIN("NIN", "Nin"),
    DRIVERS_LICENSE("DRIVERS_LICENSE", "Driver's License"),
    INTERNATIONAL_PASSPORT("PASSPORT", "International Passport"),
    VOTERS_CARD("VOTER_ID", "Voter's Card"),
    BANK_ACCOUNT("BANK_ACCOUNT", "Bank Account"),
    NONE("", "");

    private final String name;
    private final String displayName;

    public static IdCardType fromName(String name) {
        return switch (name) {
            case "NIN_SLIP", "NIN", "NIN_V2" -> NIN;
            case "bvn", "BVN", "Bvn" -> BVN;
            case "DRIVERS_LICENSE" -> DRIVERS_LICENSE;
            case "PASSPORT", "INTERNATIONAL_PASSPORT" -> INTERNATIONAL_PASSPORT;
            case "VOTER_ID", "VOTERS_CARD" -> VOTERS_CARD;
            default -> NONE;
        };
    }
}
