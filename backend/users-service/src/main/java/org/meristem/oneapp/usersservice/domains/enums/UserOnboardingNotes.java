package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserOnboardingNotes {
    APPROVED("Requirement completed"),
    FAILED("Requirement failed");

    public final String note;
}
