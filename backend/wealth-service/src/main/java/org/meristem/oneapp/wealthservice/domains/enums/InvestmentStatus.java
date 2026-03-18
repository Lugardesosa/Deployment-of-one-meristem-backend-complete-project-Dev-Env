package org.meristem.oneapp.wealthservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvestmentStatus {

    ONGOING("Ongoing"), CLOSED("Closed");

    private final String value;
}
