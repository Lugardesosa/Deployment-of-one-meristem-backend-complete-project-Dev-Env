package org.meristem.oneapp.walletservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;

@Getter
@AllArgsConstructor
public enum TransactionType {

    DEPOSIT("DEPOSIT", 1),
    WITHDRAWAL("WITHDRAWAL", 2),
    INVESTMENT("INVESTMENT", 3);

    private final String name;
    private final int value;

    public static TransactionType fromName(String name) {
        return switch (name) {
            case "DEPOSIT" -> DEPOSIT;
            case "WITHDRAWAL" -> WITHDRAWAL;
            case "INVESTMENT" -> INVESTMENT;
            default -> throw new BadRequestException("Unknown transaction type");
        };
    }

    public static TransactionType fromValue(int value) {
        return switch (value) {
            case 1 -> DEPOSIT;
            case 2 -> WITHDRAWAL;
            case 3 -> INVESTMENT;
            default -> throw new BadRequestException("Unknown transaction type");
        };
    }
}
