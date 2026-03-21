package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MandateType {

    SINGLE_TO_SIGN(1),
    BOTH_TO_SIGN(2);

    private final Integer value;
}
