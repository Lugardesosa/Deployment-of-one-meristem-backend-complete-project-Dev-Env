package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SourceOfIncome {

    PROCEEDS_FROM_INVESTMENT("Proceeds from investment"),
    SALARY("Salary"),
    BUSINESS_EARNINGS("Business earnings"),
    GIFTS_FROM_RELATIVES("Gifts from relatives"),
    PENSIONS("Pensions"),
    ALLOWANCES_BONUSES("Allowances/Bonuses"),
    ALL_OF_THE_ABOVE("All of the above");

    private final String name;
}
