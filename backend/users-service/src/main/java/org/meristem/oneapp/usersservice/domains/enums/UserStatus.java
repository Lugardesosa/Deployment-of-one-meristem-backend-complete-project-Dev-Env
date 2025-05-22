package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    ACTIVE(1),
    LOCKED(2),
    DEACTIVATED(3);

    private final Integer value;
}
