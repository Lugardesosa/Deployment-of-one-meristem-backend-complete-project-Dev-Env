package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Requirements {

    BVN(1, "BVN"),
    UTILITY_BILL(2, "UTILITY_BILL"),
    PASSPORT(3, "PASSPORT"),
    GOVERNMENT_ISSUED_ID(4, "GOVERNMENT_ISSUED_ID"),
    SIGNATURE(5, "SIGNATURE"),
    PROOF_OF_ADDRESS(6, "PROOF_OF_ADDRESS"),
    LIVENESS_CHECK(7, "LIVENESS_CHECK"),
    NEXT_OF_KIN(8, "NEXT_OF_KIN");

    private final int value;
    private final String name;
}
