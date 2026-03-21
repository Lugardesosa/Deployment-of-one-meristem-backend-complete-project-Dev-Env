package org.meristem.oneapp.notificationservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeviceOs {

    ANDROID(1, "ANDROID"),
    IOS(0, "IOS");

    private final Integer value;
    private final String label;
}
