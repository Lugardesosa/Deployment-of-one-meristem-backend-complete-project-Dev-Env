package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequirementType {

    USER(1, "USER"), // USER has to complete the process
    MACHINE(2, "MACHINE"); // The verification happens in the background

    private final int id;
    private final String name;
}
