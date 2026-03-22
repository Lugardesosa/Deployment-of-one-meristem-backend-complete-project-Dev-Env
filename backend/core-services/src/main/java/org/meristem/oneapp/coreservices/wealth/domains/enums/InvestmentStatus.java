package org.meristem.oneapp.coreservices.wealth.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvestmentStatus {

    ONGOING("Ongoing"), CLOSED("Closed");

    private final String value;
}
