package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum AmlEntityType {
    USER("USER", "Person");

    private final String name;
    private final String pastelName;
}
