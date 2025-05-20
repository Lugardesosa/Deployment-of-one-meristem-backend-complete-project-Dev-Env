package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SmileIdType {
    BVN("BVN"),
    PASSPORT("PASSPORT"),
    DRIVERS_LICENSE("DRIVERS_LICENSE"),
    VOTER_ID("VOTER_ID"),
    NIN_V2("NIN_V2");

    private final String value;
}
