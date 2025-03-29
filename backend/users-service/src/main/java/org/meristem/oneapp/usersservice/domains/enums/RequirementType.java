package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequirementType {

    BVN(1, "BVN"),
    PHONE_OTP(2, "PHONE_OTP"),
    EMAIL_OTP(3, "EMAIL_OTP"),
    UTILITY_BILL(4, "UTILITY_BILL"),
    PASSPORT(5, "PASSPORT"),
    GOVERNMENT_ISSUED_ID(6, "GOVERNMENT_ISSUED_ID"),
    SIGNATURE(7, "SIGNATURE");

    private final int value;
    private final String name;
}
