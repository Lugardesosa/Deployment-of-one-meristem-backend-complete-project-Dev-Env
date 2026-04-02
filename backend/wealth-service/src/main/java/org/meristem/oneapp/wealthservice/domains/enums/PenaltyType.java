package org.meristem.oneapp.wealthservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PenaltyType {

    NONE(1),
    PERCENTAGE(2);

    private final Integer value;
}
