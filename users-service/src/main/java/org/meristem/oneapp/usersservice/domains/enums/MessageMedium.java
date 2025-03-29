package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageMedium {
    EMAIL(1),
    SMS(2),
    WHATSAPP(3),;

    private final int value;

    public static MessageMedium of(int value) {
        return switch (value) {
            case 1 -> EMAIL;
            case 2 -> SMS;
            case 3 -> WHATSAPP;
            default -> null;
        };
    }
}
