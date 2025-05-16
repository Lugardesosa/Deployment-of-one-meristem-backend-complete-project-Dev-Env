package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;

@Getter
@AllArgsConstructor
public enum OnboardingRequirements {

    BVN(1,  "BVN"),
    PROOF_OF_ADDRESS(2, "PROOF_OF_ADDRESS"),
    NIN(3, "NIN"),
    GOVERNMENT_ISSUED_ID(4, "GOVERNMENT_ISSUED_ID");

    private final int value;
    private final String name;

    public static OnboardingRequirements of(String name) {
        return switch (name) {
            case "BVN" -> BVN;
            case "PROOF_OF_ADDRESS" -> PROOF_OF_ADDRESS;
            case "NIN" -> NIN;
            case "GOVERNMENT_ISSUED_ID" -> GOVERNMENT_ISSUED_ID;
            default -> throw new BadRequestException("Invalid requirement: " + name);
        };
    }
}
