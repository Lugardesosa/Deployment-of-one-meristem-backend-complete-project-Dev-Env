package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum AccountType {

    INDIVIDUAL(0),
    JOINT(1),
    BOTH(2),
    MINOR(3),
    CORPORATE(4),
    NOT_SET(100);

    private final Integer value;

    public static AccountType fromString(String value) {
        return Arrays.stream(AccountType.values()).filter(a -> a.name().equalsIgnoreCase(value)).findFirst().orElse(AccountType.NOT_SET);
    }
}
