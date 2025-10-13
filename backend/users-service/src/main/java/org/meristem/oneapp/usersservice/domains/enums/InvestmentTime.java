package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvestmentTime {

    LESS_THAN_1_YEAR("Less than 1 year"),
    BETWEEN_1_3_YEARS("Between 1 - 3 years"),
    BETWEEN_4_6_YEARS("Between 4 - 6 years"),
    OVER_6_YEARS("Over 6 years");

    private final String label;
}
