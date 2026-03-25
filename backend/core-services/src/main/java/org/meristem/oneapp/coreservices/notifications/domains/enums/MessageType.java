package org.meristem.oneapp.coreservices.notifications.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {

    OTP(1, "otp"),
    MARKETING(2, "marketing"),
    NOTIFICATION(3, "notification"),
    PASSWORD_RESET(4, "password_reset"),
    ADMIN_ACCOUNT_CREATED(5, "admin_account_created"),
    LOGIN_SUCCESSFUL(6, "login_successful"),
    EMAIL_CONFIRMATION(7, "");

    private final int value;
    private final String label;
}
