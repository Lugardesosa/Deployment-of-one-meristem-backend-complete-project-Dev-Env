package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AmlDecisionType {

    CLEARED("CLEARED"), REJECTED("REJECTED"), ESCALATED("ESCALATED");

    private final String name;
}
