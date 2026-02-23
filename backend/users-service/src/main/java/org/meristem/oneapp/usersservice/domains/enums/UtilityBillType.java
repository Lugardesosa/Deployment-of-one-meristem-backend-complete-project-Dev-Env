package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UtilityBillType {

    NOTARISED_DOCUMENT("NOTARISED_DOCUMENT", 1),
    ELECTRICITY_BILL("ELECTRICITY_BILL", 2),
    WATER_BILL("WATER_BILL", 3),
    WASTE_BILL("WASTE_BILL", 4),
    INTERNET_BILL("INTERNET_BILL", 5),
    BANK_STATEMENT("BANK_STATEMENT", 6),
    FULL_TENANCY_AGREEMENT("FULL_TENANCY_AGREEMENT", 7),
    LEASE_AGREEMENT("LEASE_AGREEMENT", 8),
    LAND_USE_CHARGE("LAND_USE_CHARGE", 9);


    private final String name;
    private final int value;
}
