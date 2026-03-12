package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InvestmentInstruments {

    MWML("MWML"),
    MSBL("MSBL"),
    MTL("MTL");

    private final String value;
}
