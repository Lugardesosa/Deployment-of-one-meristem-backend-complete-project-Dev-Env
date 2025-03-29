package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeatureType {

    STOCK(1),
    TREASURY_BILLS(2),
    MUTUAL_FUNDS(3);

    private final int value;
}
