package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageMedium {
    EMAIL(1, "EMAIL"),
    SMS(2, "SMS"),
    WHATSAPP(3, "WHATSAPP");

    private final int value;
    private final String label;

    public static MessageMedium of(int value) {
        return switch (value) {
            case 1 -> EMAIL;
            case 2 -> SMS;
            case 3 -> WHATSAPP;
            default -> null;
        };
    }
}
