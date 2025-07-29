package org.meristem.oneapp.coreservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormType {

    INTEGER("INTEGER"),
    STRING("STRING"),
    MONEY("MONEY"),
    NUMBER("NUMBER"),
    PHONE_NUMBER("PHONE_NUMBER"),
    EMAIL("EMAIL"),
    FILE("FILE"),
    DATE("DATE"),
    SELECTION("SELECTION");

    private final String value;

    public static FormType fromValue(final String value) {
        return switch (value) {
            case "INTEGER" -> INTEGER;
            case "STRING" -> STRING;
            case "MONEY" -> MONEY;
            case "NUMBER" -> NUMBER;
            case "PHONE_NUMBER" -> PHONE_NUMBER;
            case "EMAIL" -> EMAIL;
            case "FILE" -> FILE;
            case "DATE" -> DATE;
            case "SELECTION" -> SELECTION;
            default -> throw new IllegalArgumentException("Unknown form type: " + value);
        };
    }
}
