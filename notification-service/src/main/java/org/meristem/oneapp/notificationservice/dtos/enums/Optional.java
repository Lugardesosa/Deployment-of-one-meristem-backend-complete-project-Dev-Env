package org.meristem.oneapp.notificationservice.dtos.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Optional {

    OPTIONAL(0),
    MANDATORY(1);

    private final Integer value;
}
