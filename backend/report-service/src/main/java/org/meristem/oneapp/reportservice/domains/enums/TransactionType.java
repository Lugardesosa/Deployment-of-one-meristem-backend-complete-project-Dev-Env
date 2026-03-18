package org.meristem.oneapp.reportservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.reportservice.exception.exceptions.BadRequestException;

@Getter
@AllArgsConstructor
public enum TransactionType {

    BUY(1), SELL(2), DEPOSIT(3), WITHDRAW(4), INTEREST(5), DIVIDEND(6), FEE(7);

    private final Integer value;

    public static TransactionType fromValue(int value) {
        return switch (value) {
            case 1 -> BUY;
            case 2 -> SELL;
            case 3 -> DEPOSIT;
            case 4 -> WITHDRAW;
            case 5 -> INTEREST;
            case 6 -> DIVIDEND;
            case 7 -> FEE;
            default -> throw new BadRequestException("Unknown transaction type");
        };
    }
}
