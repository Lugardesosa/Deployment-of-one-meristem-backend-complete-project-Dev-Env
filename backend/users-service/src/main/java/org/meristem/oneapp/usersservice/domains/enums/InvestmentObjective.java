package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum InvestmentObjective {

    GROWTH("Grow wealth by saving"),
    QUICK_RETURNS("Quick returns"),
    LONG_TERM_GROWTH("Long term growth");

    private final String label;
}
