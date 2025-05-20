package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OnboardingStatus {

    PENDING(2),
    APPROVED(1),
    REJECTED(0);

    private final Integer value;
}
