package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OnboardingStage {

    EMAIL("EMAIL"),
    PASSWORD("PASSWORD");

    private final String stage;
}
