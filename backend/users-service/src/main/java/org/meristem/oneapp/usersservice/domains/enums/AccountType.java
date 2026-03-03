package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountType {

    INDIVIDUAL(0),
    JOINT(1),
    BOTH(2),
    MINOR(3),
    CORPORATE(4);

    private final Integer value;
}
