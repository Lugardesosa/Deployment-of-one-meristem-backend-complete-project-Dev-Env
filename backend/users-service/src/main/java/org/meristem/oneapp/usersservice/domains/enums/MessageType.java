package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {

    OTP(1),
    MARKETING(2),
    NOTIFICATION(3);

    private final int value;
}
