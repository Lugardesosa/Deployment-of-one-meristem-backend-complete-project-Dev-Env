package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaritalStatus {

    SINGLE("SINGLE"),
    DIVORCED("DIVORCED"),
    WIDOWED("WIDOWED"),
    MARRIED("MARRIED");

    private final String label;
}
