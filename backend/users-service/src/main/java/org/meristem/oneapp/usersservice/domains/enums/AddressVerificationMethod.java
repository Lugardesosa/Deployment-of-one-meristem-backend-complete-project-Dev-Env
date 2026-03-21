package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AddressVerificationMethod {

    AUTO_OKHI(0),
    MANUAL(1),
    BVN(2);

    private final int value;
}
