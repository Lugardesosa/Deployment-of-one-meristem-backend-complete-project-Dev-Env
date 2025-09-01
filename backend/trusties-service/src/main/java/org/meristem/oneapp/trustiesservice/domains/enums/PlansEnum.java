package org.meristem.oneapp.trustiesservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlansEnum {

    SIMPLE_WILL("Simple Will", 14),
    COMPREHENSIVE_WILL("Comprehensive Will", 15),
    PRIVATE_TRUSTS("Private Trusts", 17);

    private final String name;
    private final int value;
}
