package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvestmentDuration {

    ZERO_12_MONTH("0 - 12 months"),
    ONE_TO_FIVE_YEARS("1 - 5 years"),
    SIX_TO_TEN_YEARS("6 - 10 years"),
    OVER_TEN_YEARS("Over 10 years");

    private final String label;
}
