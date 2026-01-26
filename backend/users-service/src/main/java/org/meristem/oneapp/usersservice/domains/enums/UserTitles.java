package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTitles {

    MR("Mr", 1),
    DR("Dr", 2),
    MRS("Mrs", 3),
    CHIEF("Chief", 4);

    private final String value;
    private final Integer number;
}
