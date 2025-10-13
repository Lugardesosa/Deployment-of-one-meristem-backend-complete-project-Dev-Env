package org.meristem.oneapp.reportservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.reportservice.exception.exceptions.BadRequestException;

@Getter
@AllArgsConstructor
public enum TransactionStatus {

    COMPLETED(1, "COMPLETED"),
    PENDING(2, "PENDING"),
    FAILED(3, "FAILED");

    private final int value;
    private final String name;

    public static TransactionStatus fromName(String name) {
        return switch (name) {
            case "COMPLETED" -> COMPLETED;
            case "PENDING" -> PENDING;
            case "FAILED" -> FAILED;
            default -> throw new BadRequestException("Unknown transaction status: " + name);
        };
    }

    public static TransactionStatus fromValue(int value) {
        return switch (value) {
            case 1 -> COMPLETED;
            case 2 -> PENDING;
            case 3 -> FAILED;
            default -> throw new BadRequestException("Unknown transaction status: " + value);
        };
    }
}
