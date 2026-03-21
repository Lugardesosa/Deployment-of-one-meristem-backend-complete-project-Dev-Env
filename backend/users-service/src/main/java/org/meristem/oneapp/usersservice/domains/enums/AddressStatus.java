package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressStatus {

    FAILED(4),
    CANCELLED(3),
    PENDING(2),
    APPROVED(1),
    REJECTED(0);

    private final Integer value;
}
