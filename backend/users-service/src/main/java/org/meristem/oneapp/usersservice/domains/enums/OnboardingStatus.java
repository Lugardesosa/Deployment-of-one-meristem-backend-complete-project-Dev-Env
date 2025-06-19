package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OnboardingStatus {

    NOT_STARTED(3),
    PENDING(2),
    APPROVED(1),
    REJECTED(0);

    private final Integer value;
}
