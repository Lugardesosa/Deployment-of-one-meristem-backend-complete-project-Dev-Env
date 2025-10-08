package org.meristem.oneapp.trusteesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CompanyType {

    PUBLIC("Public"),
    PRIVATE("Private");

    private final String value;
}
