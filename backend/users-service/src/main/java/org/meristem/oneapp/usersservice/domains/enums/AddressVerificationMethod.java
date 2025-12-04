package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AddressVerificationMethod {

    AUTO(0),
    MANUAL(1);

    private final int value;
}
