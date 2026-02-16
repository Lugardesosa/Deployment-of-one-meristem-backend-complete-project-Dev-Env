package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KycQueryStatus {

    STARTED(0),
    COMPLETED(1),
    PENDING(2),
    FAILED(4);

    private final Integer value;
}
