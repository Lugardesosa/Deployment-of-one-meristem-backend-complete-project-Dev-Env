package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserPinStatus {
    ACTIVE(1),
    LOCKED(2);

    private final Integer status;
}
