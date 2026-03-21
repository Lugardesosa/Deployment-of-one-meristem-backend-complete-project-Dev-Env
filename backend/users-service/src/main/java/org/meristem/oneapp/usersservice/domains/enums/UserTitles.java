package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTitles {

    MR("Mr", "MR", "Mr.", "MR.", 1),
    DR("Dr", "DR", "Dr.", "DR.", 2),
    MRS("Mrs", "MRS", "Mrs.", "MRS.", 3),
    CHIEF("Chief", "CHF", "Chf", "CHIEF", 4),
    MISS("Miss", "MISS", "Ms.", "MISS.", 5);

    private final String small;
    private final String capAbbr;
    private final String smallFull;
    private final String capFull;
    private final Integer number;

    public static UserTitles fromString(String title) {
        if (title == null) {
            return null;
        }

        return switch (title.toLowerCase().trim()) {
            case "mr", "mr." -> MR;
            case "dr", "dr." -> DR;
            case "mrs", "mrs." -> MRS;
            case "chief", "chf" -> CHIEF;
            case "miss", "ms", "ms." -> MISS;
            default -> null;
        };
    }

}
