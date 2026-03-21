package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE("MALE", "M", "Male", "male"),
    FEMALE("FEMALE", "F", "Female", "female"),
    OTHER("OTHER", "O", "Other", "other");

    private final String caps;
    private final String abbreviation;
    private final String capitalized;
    private final String smallcaps;

    public static Gender getGender(String gender) {
        return switch (gender) {
            case "M", "MALE", "Male", "male" -> MALE;
            case "F", "FEMALE", "Female", "female" -> FEMALE;
            default -> OTHER;
        };
    }
}
