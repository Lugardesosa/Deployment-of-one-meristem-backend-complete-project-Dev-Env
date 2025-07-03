package org.meristem.oneapp.reportservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.reportservice.exception.exceptions.BadRequestException;

@Getter
@AllArgsConstructor
public enum TransactionType {

    DEPOSIT(1, "DEPOSIT"),
    WITHDRAWAL(2, "WITHDRAWAL"),
    INVESTMENT(3, "INVESTMENT");

    private final int value;
    private final String name;

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
