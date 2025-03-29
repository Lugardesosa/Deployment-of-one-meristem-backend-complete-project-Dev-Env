package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequirementType {

    BVN(1),
    EMAIL_OTP(2),
    PHONE_OTP(3),
    UTILITY_BILL(4),
    PASSPORT(5),
    GOVERNMENT_ISSUED_ID(6),
    SIGNATURE(7);

    private final int value;
}
