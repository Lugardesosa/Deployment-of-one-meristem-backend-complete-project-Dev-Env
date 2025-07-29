package org.meristem.oneapp.coreservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormName {

    CASH("Cash", 1),
    PUBLIC_EQUITIES("Public Equities", 2),
    PRIVATE_EQUITIES("Private Equities", 3),
    FINTECH_WALLET("Fintech Wallets", 4),
    REAL_ESTATE("Real Estate", 5),
    FIXED_INCOME_MONEY_MARKET("Fixed Income/Money Market", 6),
    INTELLECTUAL_PROPERTY("Intellectual Property", 7),
    ALTERNATE_ASSETS("Alternate Assets", 8),
    CRYPTOCURRENCY_AND_NFT("Cryptocurrencies & NFT", 9),
    DIGITAL_PLATFORM("Digital Platform", 10),
    PERSONAL_ASSETS("Personal Assets", 11),
    PENSION("Pension", 12),
    LIFE_INSURANCE("Life Insurance", 13);

    private final String displayName;
    private final Integer position;

    public static FormName fromName(String name) {
        return switch (name) {
            case "Cash" -> CASH;
            case "Public Equities" -> PUBLIC_EQUITIES;
            case "Private Equities" -> PRIVATE_EQUITIES;
            case "Fintech Wallets" -> FINTECH_WALLET;
            case "Real Estate" -> REAL_ESTATE;
            case "Fixed Income/Money Market"  -> FIXED_INCOME_MONEY_MARKET;
            case "Intellectual Property" -> INTELLECTUAL_PROPERTY;
            case "Alternate Assets" -> ALTERNATE_ASSETS;
            case "Personal Assets" -> PERSONAL_ASSETS;
            case "Pension" -> PENSION;
            case "Life Insurance" -> LIFE_INSURANCE;
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }

    public static FormName fromPosition(Integer position) {
        return switch (position) {
            case 1 -> CASH;
            case 2 -> PUBLIC_EQUITIES;
            case 3 -> PRIVATE_EQUITIES;
            case 4 -> FINTECH_WALLET;
            case 5 -> REAL_ESTATE;
            case 6 -> FIXED_INCOME_MONEY_MARKET;
            case 7 -> INTELLECTUAL_PROPERTY;
            case 8 -> ALTERNATE_ASSETS;
            case 9 -> PERSONAL_ASSETS;
            case 10 -> PENSION;
            case 11 -> LIFE_INSURANCE;
            default -> throw new IllegalStateException("Unexpected value: " + position);
        };
    }
}
