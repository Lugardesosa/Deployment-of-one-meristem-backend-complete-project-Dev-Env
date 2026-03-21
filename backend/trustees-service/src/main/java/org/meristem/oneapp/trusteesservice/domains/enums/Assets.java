package org.meristem.oneapp.trusteesservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.trusteesservice.dtos.sql.RowMappers;
import org.meristem.oneapp.trusteesservice.models.*;
import org.springframework.jdbc.core.RowMapper;

@AllArgsConstructor
@Getter
public enum Assets {

    CASH("Cash", "Cash Asset", "Select Cash Asset", "CASH", Cash.class, RowMappers.getCashRowMapper()),
    PUBLIC_EQUITIES("Public Equities", "Public Equities Asset", "Select Public Equities", "PUBLIC_EQUITIES", PublicEquities.class, RowMappers.getPublicEquitiesRowMapper()),
    PRIVATE_EQUITIES("Private Equities", "Private Equities Asset", "Select Private Equities Asset", "PRIVATE_EQUITIES", PrivateEquities.class, RowMappers.getEquitiesRowMapper()),
    REAL_ESTATE("Real Estate", "Property", "Select Property", "REAL_ESTATE", RealEstate.class, RowMappers.getRealEstateRowMapper()),
    FIXED_INCOME_MONEY_MARKET("Fixed Income/Money Market", "Money Market Asset", "Select Money Market Asset", "MONEY_MARKET", MoneyMarket.class, RowMappers.getMoneyMarketRowMapper()),
    INTELLECTUAL_PROPERTY("Intellectual Property", "Property", "Select Property", "INTELLECTUAL_PROPERTY", IntellectualProperty.class,  RowMappers.getIntellectualPropertyRowMapper()),
    ALTERNATE_ASSETS("Alternate Assets", "Alternate Asset", "Select Alternate Asset", "ALTERNATE_ASSETS", AlternateAssets.class,  RowMappers.getAlternateAssetsRowMapper()),
    PERSONAL_ASSETS("Personal Assets", "Personal Asset", "Select Personal Asset", "PERSONAL_ASSET", PersonalAssets.class,  RowMappers.getPersonalAssetsRowMapper()),
    PENSION("Pension", "Pension Asset", "Select Pension Asset", "PENSION", Pension.class,  RowMappers.getPensionRowMapper()),
    LIFE_INSURANCE("Life Insurance", "Life Insurance Asset", "Select Life Insurance Asset", "LIFE_INSURANCE", LifeInsurance.class,  RowMappers.getLifeInsuranceRowMapper()),;


    private final String displayName;
    private final String label;
    private final String placeholder;
    private final String name;
    private final Class<?> clazz;
    private final RowMapper<?> rowMapper;

    public static Assets fromValue(String name) {
        return switch (name) {
            case "CASH" -> CASH;
            case "PUBLIC_EQUITIES" -> PUBLIC_EQUITIES;
            case "PRIVATE_EQUITIES" -> PRIVATE_EQUITIES;
            case "REAL_ESTATE" -> REAL_ESTATE;
            case "FIXED_INCOME_MONEY" -> FIXED_INCOME_MONEY_MARKET;
            case "INTELLECTUAL_PROPERTY" -> INTELLECTUAL_PROPERTY;
            case "ALTERNATE_ASSETS" -> ALTERNATE_ASSETS;
            case "PERSONAL_ASSETS" -> PERSONAL_ASSETS;
            case "PENSION" -> PENSION;
            case "LIFE_INSURANCE" -> LIFE_INSURANCE;
            default -> null;
        };
    }
}
