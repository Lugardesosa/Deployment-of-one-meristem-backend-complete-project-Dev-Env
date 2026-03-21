package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AmlResultStatus {
    UNUSED(0),
    USED(1);

    private final Integer value;
}
