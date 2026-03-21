package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AggregateType {

    USER("USER"),
    OTP("OTP");

    private final String value;
}
