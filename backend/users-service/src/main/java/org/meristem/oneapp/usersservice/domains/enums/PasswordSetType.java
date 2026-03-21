package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PasswordSetType {

    INDIVIDUAL,
    PRIMARY,
    SECONDARY,
    EXISTING
}
