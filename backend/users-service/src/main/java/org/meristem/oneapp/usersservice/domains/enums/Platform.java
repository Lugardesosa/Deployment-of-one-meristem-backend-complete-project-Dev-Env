package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {

    WEB("WEB"),
    MOBILE("MOBILE");

    private final String value;
}
