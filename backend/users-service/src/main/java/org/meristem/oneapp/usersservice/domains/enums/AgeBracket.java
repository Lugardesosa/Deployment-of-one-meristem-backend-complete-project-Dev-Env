package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AgeBracket {
    EIGHTEEN_34("18 - 34 years"),
    THIRTY_FIVE_49("35 - 49 years"),
    FIFTY_64("50 - 64 years"),
    SIXTY_FIVE_ABOVE("65 years and above");

    private final String name;
}
