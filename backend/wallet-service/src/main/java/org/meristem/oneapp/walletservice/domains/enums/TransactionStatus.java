package org.meristem.oneapp.walletservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;

@Getter
@AllArgsConstructor
public enum TransactionStatus {

    COMPLETED("COMPLETED", 1),
    PENDING("PENDING", 2),
    FAILED("FAILED", 3);

    private final String name;
    private final int value;

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
