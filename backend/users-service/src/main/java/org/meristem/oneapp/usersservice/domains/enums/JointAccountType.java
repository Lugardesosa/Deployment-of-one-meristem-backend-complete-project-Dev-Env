package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JointAccountType {

    PRIMARY(1, "PRIMARY"),
    SECONDARY(2, "SECONDARY");

    private final Integer value;
    private final String name;
}
