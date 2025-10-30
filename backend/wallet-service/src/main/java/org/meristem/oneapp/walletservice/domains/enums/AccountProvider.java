package org.meristem.oneapp.walletservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;

@AllArgsConstructor
@Getter
public enum AccountProvider {

    WEMA("WEMA", "Wema", "035"),
    PROVIDUS("PROVIDUS", "Xpresswallet","101");

    private final String value;
    private final String bankName;
    private final String bankCode;

    public static AccountProvider of(String value) {
        return switch (value) {
            case "WEMA", "Wema", "035" -> WEMA;
            case "PROVIDUS", "Xpresswallet", "101" -> PROVIDUS;
            default -> throw new BadRequestException("Invalid account provider value: " + value);
        };
    }
}
