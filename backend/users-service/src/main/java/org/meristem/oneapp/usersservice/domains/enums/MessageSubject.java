package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The otp types and their code, and subject body used when sending the
 * otp out
 */
@Getter
@AllArgsConstructor
public enum MessageSubject {

    EMAIL_VERIFICATION(1, "Email verification OTP received "),
    PASSWORD_RESET(2, "Password reset OTP received "),
    ONBOARDING_VERIFICATION (3, "Onboarding Verification OTP received "),
    LOGIN_ALERT (4, "There was a log into your account "),
    JOINT_EMAIL_VERIFICATION (5, "Email verification OTP received "),
    SECONDARY_EMAIL_VERIFICATION (6, "Email verification OTP received "),
    EXISTING_EMAIL_VERIFICATION (7, "Email verification OTP received ");

    private final int code;
    private final String message;


    /**
     * gets the subject (string) for a particular otp type
     * @param code int
     * @return String
     */
    public static String getMessageSubject(int code) {
        for (MessageSubject messageSubject : MessageSubject.values()) {
            if (messageSubject.getCode() == code) {
                return messageSubject.getMessage();
            }
        }
        return null;
    }

    public static MessageSubject valueOf(int code) {
        for (MessageSubject messageSubject : MessageSubject.values()) {
            if (messageSubject.getCode() == code) {
                return messageSubject;
            }
        }
        return null;
    }
}
