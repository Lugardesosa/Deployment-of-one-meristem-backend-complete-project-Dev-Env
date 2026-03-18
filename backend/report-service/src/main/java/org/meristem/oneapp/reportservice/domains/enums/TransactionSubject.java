package org.meristem.oneapp.reportservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionSubject {

    WEALTH(1),
    STOCK(2),
    WALLET(3);

    private final Integer value;
}
