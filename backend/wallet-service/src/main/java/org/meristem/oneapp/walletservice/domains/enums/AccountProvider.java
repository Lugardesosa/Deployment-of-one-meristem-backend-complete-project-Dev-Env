package org.meristem.oneapp.walletservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;

@AllArgsConstructor
@Getter
public enum AccountProvider {

    WEMA("WEMA", "035"),
    PROVIDUS("PROVIDUS", "101");

    private final String value;
    private final String bankCode;

    public static AccountProvider fromValue(String value) {
        return switch (value) {
            case "WEMA" -> WEMA;
            case "PROVIDUS" -> PROVIDUS;
            default -> throw new BadRequestException("Invalid account provider value: " + value);
        };
    }

    public static AccountProvider fromBankCode(String bankCode) {
        return switch (bankCode) {
            case "035" -> WEMA;
            case "101" -> PROVIDUS;
            default -> throw new BadRequestException("Invalid account provider bank code: " + bankCode);
        };
    }
}
