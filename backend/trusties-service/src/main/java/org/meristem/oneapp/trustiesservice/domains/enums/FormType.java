package org.meristem.oneapp.trustiesservice.domains.enums;

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
    HEADER("HEADER"),
    ADD_MORE("ADD_MORE"),
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
            case "HEADER" -> HEADER;
            case "ADD_MORE" -> ADD_MORE;
            case "SELECTION" -> SELECTION;
            default -> throw new IllegalArgumentException("Unknown form type: " + value);
        };
    }
}
