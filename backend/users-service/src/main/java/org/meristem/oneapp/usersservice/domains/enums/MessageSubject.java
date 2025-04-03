//package org.meristem.oneapp.usersservice.domains.enums;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//@Getter
//@AllArgsConstructor
//public enum MessageSubject {
//
//    REGISTRATION(1, "Registration OTP successfully sent to "),
//    PASSWORD_RESET(2, "Password reset OTP successfully sent to ");
//
//    private final int code;
//    private final String message;
//
//    public static String getMessageSubject(int code) {
//        for (MessageSubject messageSubject : MessageSubject.values()) {
//            if (messageSubject.getCode() == code) {
//                return messageSubject.getMessage();
//            }
//        }
//        return null;
//    }
//}
