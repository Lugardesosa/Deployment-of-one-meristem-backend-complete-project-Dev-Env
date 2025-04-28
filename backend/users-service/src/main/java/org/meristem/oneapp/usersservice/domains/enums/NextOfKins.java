package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NextOfKins {

    WIFE("Wife"),
    HUSBAND("Husband"),
    PARTNER("Partner"),
    MOTHER("Mother"),
    FATHER("Father"),
    BROTHER("Brother"),
    SISTER("Sister"),
    COUSIN("Cousin"),
    SON("Son"),
    DAUGHTER("Daughter"),
    UNCLE("Uncle"),
    AUNTY("Aunty"),
    NEPHEW("Nephew"),
    NIECE("Niece"),
    LEGAL_GUARDIAN("Legal Guardian"),
    OTHERS("Others");

    private final String displayName;

    public static NextOfKins fromName(String name) {
        for (NextOfKins nextOfKins : NextOfKins.values()) {
            if (nextOfKins.name().equals(name)) {
                return nextOfKins;
            }
        }
        return OTHERS;
    }
}
