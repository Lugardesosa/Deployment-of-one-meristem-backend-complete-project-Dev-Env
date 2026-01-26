package org.meristem.oneapp.walletservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AggregateType {

    WALLET("WALLET"), ACCOUNT("ACCOUNT");
    private final String value;
}
