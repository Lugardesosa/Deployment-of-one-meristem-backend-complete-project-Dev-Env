package org.meristem.oneapp.coreservices.notifications.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {

    INACTIVE(0),
    ACTIVE(1);

    private final int value;
}
