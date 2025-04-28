package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Roles {

    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN"),
    USER("USER");

    private final String name;
}