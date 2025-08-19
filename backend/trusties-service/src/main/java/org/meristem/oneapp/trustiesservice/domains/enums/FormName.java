package org.meristem.oneapp.trustiesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.trustiesservice.constants.AppConstants;

@Getter
@AllArgsConstructor
public enum FormName {

    CASH("Cash", 1, AppConstants.FORM_TYPE_ASSET),
    PUBLIC_EQUITIES("Public Equities", 2, AppConstants.FORM_TYPE_ASSET),
    PRIVATE_EQUITIES("Private Equities", 3, AppConstants.FORM_TYPE_ASSET),
    REAL_ESTATE("Real Estate", 4, AppConstants.FORM_TYPE_ASSET),
    FIXED_INCOME_MONEY_MARKET("Fixed Income/Money Market", 5, AppConstants.FORM_TYPE_ASSET),
    INTELLECTUAL_PROPERTY("Intellectual Property", 6, AppConstants.FORM_TYPE_ASSET),
    ALTERNATE_ASSETS("Alternate Assets", 7, AppConstants.FORM_TYPE_ASSET),
    CRYPTOCURRENCY_AND_NFT("Cryptocurrencies & NFT", 8, AppConstants.FORM_TYPE_ASSET),
    DIGITAL_PLATFORM("Digital Platform", 9, AppConstants.FORM_TYPE_ASSET),
    FINTECH_WALLET("Fintech Wallets", 10, AppConstants.FORM_TYPE_ASSET),
    PERSONAL_ASSETS("Personal Assets", 11, AppConstants.FORM_TYPE_ASSET),
    PENSION("Pension", 12, AppConstants.FORM_TYPE_ASSET),
    LIFE_INSURANCE("Life Insurance", 13, AppConstants.FORM_TYPE_ASSET),
    SIMPLE_WILL("Simple Will", 14, AppConstants.FORM_TYPE_PLAN),
    COMPREHENSIVE_WILL("Comprehensive Will", 15, AppConstants.FORM_TYPE_PLAN),
    EDUCATION_TRUST("Education Trust", 16, AppConstants.FORM_TYPE_PLAN),
    LIVING_TRUST("Living Trust", 17, AppConstants.FORM_TYPE_PLAN),
    NOMINATED_FUND("Nominated Fund", 18, AppConstants.FORM_TYPE_PLAN),
    TESTAMENTARY_TRUST("Testamentary Trust", 19, AppConstants.FORM_TYPE_PLAN);

    private final String displayName;
    private final Integer position;
    private final String type;
}
