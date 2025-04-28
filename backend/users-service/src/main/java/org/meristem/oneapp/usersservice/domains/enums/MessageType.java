package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {

    OTP(1),
    MARKETING(2),
    NOTIFICATION(3),
    PASSWORD_RESET(4),
    ADMIN_ACCOUNT_CREATED(5);

    private final int value;
}
