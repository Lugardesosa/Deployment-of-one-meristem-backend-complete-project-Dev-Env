package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UtilityBillType {

    ELECTRICITY_BILL("ELECTRICITY_BILL", 1),
    TENANCY_AGREEMENT("TENANCY_AGREEMENT", 2),
    WATER_BILL("WATER_BILL", 3),
    WASTE_BILL("WASTE_BILL", 4);


    private final String name;
    private final int value;
}
