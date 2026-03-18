package org.meristem.oneapp.reportservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityType {

    BUY, SELL, DEPOSIT, WITHDRAW, INTEREST, DIVIDEND, FEE;
}
