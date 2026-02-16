package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    ACTIVE(1),
    LOCKED(2),
    DEACTIVATED(3),
    EMAIL_NOT_VERIFIED(4),
    KYC_NOT_COMPLETED(5),
    DELETED(6),
    DATA_SHARING_NOT_COMPLETED(7);

    private final Integer value;
}
