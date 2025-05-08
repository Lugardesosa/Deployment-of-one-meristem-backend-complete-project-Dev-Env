package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The otp types and their code, and subject body used when sending the
 * otp out
 */
@Getter
@AllArgsConstructor
public enum OtpType {

    REGISTRATION(1, "Registration OTP received "),
    PASSWORD_RESET(2, "Password reset OTP received ");

    private final int code;
    private final String message;


    /**
     * gets the subject (string) for a particular otp type
     * @param code int
     * @return String
     */
    public static String getMessageSubject(int code) {
        for (OtpType messageSubject : OtpType.values()) {
            if (messageSubject.getCode() == code) {
                return messageSubject.getMessage();
            }
        }
        return null;
    }

    public static OtpType valueOf(int code) {
        for (OtpType messageSubject : OtpType.values()) {
            if (messageSubject.getCode() == code) {
                return messageSubject;
            }
        }
        return null;
    }
}
