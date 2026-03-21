package org.meristem.oneapp.trusteesservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MarriageType {

    MARRIAGE_BY_THE_ACT("Marriage by the Act"),
    CUSTOMARY_MARRIAGE("Customary Marriage"),
    ISLAMIC_MARRIAGE("Islamic Marriage");

    private final String value;

}
