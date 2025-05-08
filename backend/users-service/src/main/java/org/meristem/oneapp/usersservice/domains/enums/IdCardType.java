package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdCardType {

    NIN("Nin"),
    DRIVERS_LICENSE("Driver's License"),
    INTERNATIONAL_PASSPORT("International Passport"),
    VOTERS_CARD("Voter's Card");

    private final String displayName;
}
