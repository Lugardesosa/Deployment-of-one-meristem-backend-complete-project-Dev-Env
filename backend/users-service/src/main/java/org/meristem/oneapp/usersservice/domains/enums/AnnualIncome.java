package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AnnualIncome {

    LESS_THAN_1_MILLION("Less than 1 million"),
    ONE_5_MILLION("1 - 5 million"),
    FIVE_TO_10_MILLION("5 - 10 million"),
    TEN_T0_20_MILLION("10 - 20 million"),
    OVER_20_MILLION("Over 20 million");

    private final String label;
}
