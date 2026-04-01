package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;

import java.awt.datatransfer.Clipboard;

@Getter
@AllArgsConstructor
public enum OnboardingRequirements {

    BVN(1,  "BVN"),
    PROOF_OF_ADDRESS(2, "PROOF_OF_ADDRESS"),
    NIN(3, "NIN"),
    GOVERNMENT_ISSUED_ID(4, "GOVERNMENT_ISSUED_ID"),
    EMAIL_VERIFICATION(5, "EMAIL_VERIFICATION"),
    CHN_NUMBER(6, "CHN_NUMBER"),
    CSCS_NUMBER(7, "CSCS_NUMBER"),
    BVN_SIGN_UP(8, "BVN_SIGN_UP");

    private final int value;
    private final String name;

    public static OnboardingRequirements of(String name) {
        return switch (name) {
            case "BVN" -> BVN;
            case "PROOF_OF_ADDRESS" -> PROOF_OF_ADDRESS;
            case "NIN" -> NIN;
            case "INTERNATIONAL_PASSPORT", "DRIVERS_LICENSE", "VOTER_ID" -> GOVERNMENT_ISSUED_ID;
            case "EMAIL_VERIFICATION" -> EMAIL_VERIFICATION;
            case "CHN_NUMBER" -> CHN_NUMBER;
            case "CSCS_NUMBER" -> CSCS_NUMBER;
            case "BVN_SIGN_UP" -> BVN_SIGN_UP;
            default -> throw new BadRequestException("Invalid requirement: " + name);
        };
    }
}
