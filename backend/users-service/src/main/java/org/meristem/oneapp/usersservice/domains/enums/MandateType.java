package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MandateType {

    SINGLE(1),
    BOTH(2);

    private final Integer value;
}
