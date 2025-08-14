package org.meristem.oneapp.coreservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WillsEnum {

    SIMPLE_WILL("Simple Will", 14),
    COMPREHENSIVE_WILL("Comprehensive Will", 15);

    private final String name;
    private final int value;
}
