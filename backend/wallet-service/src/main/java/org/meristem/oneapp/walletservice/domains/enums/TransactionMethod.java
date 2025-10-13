package org.meristem.oneapp.walletservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;

@AllArgsConstructor
@Getter
public enum TransactionMethod {

    BANK_TRANSFER("BANK_TRANSFER", 1),
    CARD("CARD", 2),
    POS("POS", 3);

    private final String name;
    private final int value;

    public static TransactionMethod fromValue(int value) {
        return switch (value) {
            case 1 -> BANK_TRANSFER;
            case 2 -> CARD;
            case 3 -> POS;
            default -> throw new BadRequestException("Invalid transaction method value: " + value);
        };
    }

    public static TransactionMethod fromName(String name) {
        return switch (name) {
            case "BANK_TRANSFER" -> BANK_TRANSFER;
            case "CARD" -> CARD;
            case "POS" -> POS;
            default -> throw new BadRequestException("Invalid transaction method name: " + name);
        };
    }
}
