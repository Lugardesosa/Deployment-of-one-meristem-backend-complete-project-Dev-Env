package org.meristem.oneapp.coreservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Religion {

    CHRISTIANITY("Christianity"),
    ISLAM("Islam"),
    TRADITIONAL_RELIGION("Traditional Religion");

    private final String value;

}
