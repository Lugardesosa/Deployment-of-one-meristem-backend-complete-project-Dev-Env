package org.meristem.oneapp.reportservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionDirection {
    CREDIT(0), DEBIT(1);

    private final Integer value;
}
