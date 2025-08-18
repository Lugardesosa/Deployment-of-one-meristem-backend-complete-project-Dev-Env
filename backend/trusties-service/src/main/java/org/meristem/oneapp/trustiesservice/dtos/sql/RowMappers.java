package org.meristem.oneapp.trustiesservice.dtos.sql;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trustiesservice.domains.responses.*;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@UtilityClass
public final class RowMappers {

    public static RowMapper<FormResponse.Selection> getSelectionValue() {
        return (rs, rowNum) -> FormResponse.Selection.builder().selectionValue(rs.getString("selection_value"))
                .additionalValue(rs.getString("additional_value")).build();
    }

    public static RowMapper<BankResponse> getBankNames() {
        return (rs, rowNum) ->
                BankResponse.builder()
                        .name(rs.getString("name")).id(rs.getLong("id"))
                        .build();
    }

    public static RowMapper<FormResponse.Currency> getCurrencyRowMapper() {
        return (rs, rn) ->
                FormResponse.Currency.builder()
                        .currencyName(rs.getString("currency_name"))
                        .currencyLogo("currency_logo").build();
    }

    public static RowMapper<CashResponse> getCashRowMapper() {
        return (rs, rn) ->
                CashResponse.builder()
                        .id(rs.getLong("id"))
                        .accountName(rs.getString("account_name"))
                        .accountNumber(rs.getString("account_number"))
                        .accountType(rs.getString("account_type"))
                        .currencyId(rs.getLong("currency_id"))
                        .estimatedAmount(rs.getBigDecimal("estimated_amount"))
                        .otherDetails(rs.getString("other_details"))
                        .build();
    }

    public static RowMapper<PublicEquitiesResponse> getPublicEquitiesRowMapper() {
        return (rs, rn) ->
                PublicEquitiesResponse.builder()
                        .id(rs.getLong("id"))
                        .currencyId(rs.getLong("currency_id"))
                        .brokerageHouse(rs.getString("brokerage_house"))
                        .chn(rs.getString("chn"))
                        .noOfUnits(rs.getInt("no_of_units"))
                        .shareName(rs.getString("share_name"))
                        .companyType(rs.getString("company_type"))
                        .estimatedAmount(rs.getBigDecimal("estimated_amount"))
                        .otherDetails(rs.getString("other_details"))
                        .cscsNumber(rs.getString("cscs_number"))
                        .build();
    }

    public static RowMapper<EquitiesResponse> getEquitiesRowMapper() {
        return (rs, rn) ->
                EquitiesResponse.builder()
                        .id(rs.getLong("id"))
                        .currencyId(rs.getLong("currency_id"))
                        .brokerageHouse(rs.getString("brokerage_house"))
                        .noOfUnits(rs.getInt("no_of_units"))
                        .shareName(rs.getString("share_name"))
                        .companyType(rs.getString("company_type"))
                        .estimatedAmount(rs.getBigDecimal("estimated_amount"))
                        .otherDetails(rs.getString("other_details"))
                        .build();
    }


    public static RowMapper<RealEstateResponse> getRealEstateRowMapper() {
        return (rs, rn) ->
                RealEstateResponse.builder()
                        .id(rs.getLong("id"))
                        .currencyId(rs.getLong("currency_id"))
                        .estimatedAmount(rs.getBigDecimal("estimated_amount"))
                        .otherDetails(rs.getString("other_details"))
                        .propertyAddress(rs.getString("property_address"))
                        .propertyDescription(rs.getString("property_description"))
                        .propertyType(rs.getString("property_type"))
                        .build();
    }

    public static RowMapper<MoneyMarketResponse> getMoneyMarketRowMapper() {
        return (rs, rn) ->
                MoneyMarketResponse.builder()
                        .id(rs.getLong("id"))
                        .currencyId(rs.getLong("currency_id"))
                        .estimatedAmount(rs.getBigDecimal("estimated_amount"))
                        .otherDetails(rs.getString("other_details"))
                        .assetType(rs.getString("asset_type"))
                        .investmentHouse(rs.getString("investment_house"))
                        .build();
    }

    public static RowMapper<IntellectualPropertyResponse> getIntellectualPropertyRowMapper() {
        return (rs, rn) ->
                IntellectualPropertyResponse.builder()
                        .id(rs.getLong("id"))
                        .currencyId(rs.getLong("currency_id"))
                        .estimatedAmount(rs.getBigDecimal("estimated_amount"))
                        .otherDetails(rs.getString("other_details"))
                        .propertyType(rs.getString("property_type"))
                        .propertyDescription(rs.getString("property_description"))
                        .registeredName(rs.getString("registered_name"))
                        .build();
    }

    public static RowMapper<AlternateAssetsResponse> getAlternateAssetsRowMapper() {
        return (rs, rn) ->
                AlternateAssetsResponse.builder()
                        .id(rs.getLong("id"))
                        .currencyId(rs.getLong("currency_id"))
                        .estimatedAmount(rs.getBigDecimal("estimated_amount"))
                        .otherDetails(rs.getString("other_details"))
                        .assetType(rs.getString("asset_type"))
                        .platform(rs.getString("platform"))
                        .uniqueId(rs.getString("unique_id"))
                        .build();
    }

    public static RowMapper<PersonalAssetsResponse> getPersonalAssetsRowMapper() {
        return (rs, rn) ->
                PersonalAssetsResponse.builder()
                        .id(rs.getLong("id"))
                        .currencyId(rs.getLong("currency_id"))
                        .estimatedAmount(rs.getBigDecimal("estimated_amount"))
                        .otherDetails(rs.getString("other_details"))
                        .assetType(rs.getString("asset_type"))
                        .identifyingNo(rs.getString("identifying_no"))
                        .assetDescription(rs.getString("asset_description"))
                        .build();
    }

    public static RowMapper<PensionResponse> getPensionRowMapper() {
        return (rs, rn) ->
                PensionResponse.builder()
                        .id(rs.getLong("id"))
                        .currencyId(rs.getLong("currency_id"))
                        .estimatedAmount(rs.getBigDecimal("estimated_amount"))
                        .otherDetails(rs.getString("other_details"))
                        .rsa(rs.getString("rsa"))
                        .pfa(rs.getString("pfa"))
                        .build();
    }

    public static RowMapper<LifeInsuranceResponse> getLifeInsuranceRowMapper() {
        return (rs, rn) ->
                LifeInsuranceResponse.builder()
                        .id(rs.getLong("id"))
                        .currencyId(rs.getLong("currency_id"))
                        .estimatedAmount(rs.getBigDecimal("estimated_amount"))
                        .otherDetails(rs.getString("other_details"))
                        .insuranceCompany(rs.getString("insurance_company"))
                        .policyNumber(rs.getString("policy_number"))
                        .expiryDate(rs.getObject("expiry_date", LocalDate.class))
                        .build();
    }


    public static ResultSetExtractor<List<?>> getSimpleWill() {
        return (rs) ->
        {
            Map<String, SimpleWillResponse> row = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                SimpleWillResponse simpleWillResponse = row.get(id.toString());

                if (simpleWillResponse == null) {
                    simpleWillResponse = SimpleWillResponse.builder()
                            .id(id).lastName(rs.getString("last_name")).email(rs.getString("email"))
                            .address(rs.getString("address")).firstName(rs.getString("first_name")).middleName(rs.getString("middle_name"))
                            .ownerId(rs.getLong("owner_id")).maritalStatus(rs.getString("marital_status"))
                            .phoneNumber(String.valueOf(rs.getLong("phone_number"))).title(rs.getString("title"))
                            .build();
                    Set<PlanAssetResponse> planAssetResponseSet = new HashSet<>();
                    Set<PlanBeneficiariesResponse> planBeneficiariesResponseSet = new HashSet<>();

                    setAssetAndBeneficiaries(rs, planAssetResponseSet, planBeneficiariesResponseSet);

                    simpleWillResponse.setAssets(planAssetResponseSet);
                    simpleWillResponse.setBeneficiaries(planBeneficiariesResponseSet);

                    row.put(id.toString(), simpleWillResponse);
                } else {
                    setAssetAndBeneficiaries(rs, simpleWillResponse.getAssets(), simpleWillResponse.getBeneficiaries());
                }
            }
            return new ArrayList<>(row.values());
        };

    }

    public static ResultSetExtractor<List<?>> getComprehensiveWill() {
        return (rs) ->

        {
            Map<String, ComprehensiveWillResponse> row = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                ComprehensiveWillResponse comprehensiveWillResponse = row.get(id.toString());

                if (comprehensiveWillResponse == null) {
                    comprehensiveWillResponse = ComprehensiveWillResponse.builder()
                            .id(id).lastName(rs.getString("last_name")).email(rs.getString("email"))
                            .address(rs.getString("address")).firstName(rs.getString("first_name")).middleName(rs.getString("middle_name"))
                            .ownerId(rs.getLong("owner_id")).maritalStatus(rs.getString("marital_status"))
                            .phoneNumber(String.valueOf(rs.getLong("phone_number"))).title(rs.getString("title"))
                            .customaryTradition(rs.getString("customary_tradition")).marriageType(rs.getString("marriage_type"))
                            .occupation(rs.getString("occupation")).otherDetails(rs.getString("other_details"))
                            .religion(rs.getString("religion"))
                            .build();
                    Set<PlanAssetResponse> planAssetResponseSet = new HashSet<>();
                    Set<PlanBeneficiariesResponse> planBeneficiariesResponseSet = new HashSet<>();

                    setAssetAndBeneficiaries(rs, planAssetResponseSet, planBeneficiariesResponseSet);

                    comprehensiveWillResponse.setAssets(planAssetResponseSet);
                    comprehensiveWillResponse.setBeneficiaries(planBeneficiariesResponseSet);

                    row.put(id.toString(), comprehensiveWillResponse);
                } else {
                    setAssetAndBeneficiaries(rs, comprehensiveWillResponse.getAssets(), comprehensiveWillResponse.getBeneficiaries());
                }
            }
            return new ArrayList<>(row.values());
        };
    }

    public static ResultSetExtractor<List<?>> getNominatedFund() {
        return (rs) ->

        {

            Map<String, NominatedFundResponse> row = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                NominatedFundResponse nominatedFundResponse = row.get(id.toString());

                if (nominatedFundResponse == null) {
                    nominatedFundResponse = NominatedFundResponse.builder()
                            .id(id).lastName(rs.getString("last_name")).email(rs.getString("email"))
                            .address(rs.getString("address")).firstName(rs.getString("first_name"))
                            .ownerId(rs.getLong("owner_id"))
                            .phoneNumber(String.valueOf(rs.getLong("phone_number")))
                            .build();
                    Set<PlanBeneficiariesResponse> planBeneficiariesResponseSet = new HashSet<>();

                    setAssetAndBeneficiaries(rs, planBeneficiariesResponseSet);

                    nominatedFundResponse.setBeneficiaries(planBeneficiariesResponseSet);

                    row.put(id.toString(), nominatedFundResponse);
                } else {
                    setAssetAndBeneficiaries(rs, nominatedFundResponse.getBeneficiaries());
                }
            }
            return new ArrayList<>(row.values());
        };
    }

    private static void setAssetAndBeneficiaries(ResultSet rs, Set<PlanBeneficiariesResponse> planBeneficiariesResponseSet) throws SQLException {

        PlanBeneficiariesResponse planBeneficiariesResponse = PlanBeneficiariesResponse.builder().beneficiaryId(rs.getLong("beneficiary_id")).beneficiaryPercent(rs.getDouble("percentage")).build();
        planBeneficiariesResponseSet.add(planBeneficiariesResponse);
    }

    private static void setAssetAndBeneficiaries(ResultSet rs, Set<PlanAssetResponse> assets, Set<PlanBeneficiariesResponse> beneficiaries) throws SQLException {
        PlanAssetResponse planAssetResponse = PlanAssetResponse.builder().assetId(rs.getLong("asset_id")).assetType(rs.getString("asset_type")).build();
        assets.add(planAssetResponse);

        PlanBeneficiariesResponse planBeneficiariesResponse = PlanBeneficiariesResponse.builder().beneficiaryId(rs.getLong("beneficiary_id")).build();
        beneficiaries.add(planBeneficiariesResponse);
    }
}
