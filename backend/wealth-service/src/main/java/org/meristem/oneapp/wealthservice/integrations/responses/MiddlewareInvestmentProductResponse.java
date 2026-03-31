package org.meristem.oneapp.wealthservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MiddlewareInvestmentProductResponse(
        @JsonProperty("book_of_account_id") String bookOfAccountId,
        @JsonProperty("fund_id") String fundId,
        @JsonProperty("fund_description") String fundDescription,
        @JsonProperty("product_id") String productId,
        @JsonProperty("product_description") String productDescription,
        @JsonProperty("product_display_description") String productDisplayDescription,
        @JsonProperty("currency_id") String currencyId,
        @JsonProperty("currency_description") String currencyDescription,
        @JsonProperty("minimum_invest_amount") Double minimumInvestAmount,
        @JsonProperty("maximum_invest_amount") Double maximumInvestAmount,
        @JsonProperty("invest_tenor_days") Integer investTenorDays,
        @JsonProperty("tax_yn") String taxYn,
        @JsonProperty("tax_rate") Double taxRate,
        @JsonProperty("allow_new_investment_yesno") String allowNewInvestmentYesno,
        @JsonProperty("allow_investment_rollover_yesno") String allowInvestmentRolloverYesno,
        @JsonProperty("allow_premature_liquidation_yesno") String allowPrematureLiquidationYesno,
        @JsonProperty("pre_liquidation_penalty_rate") Double preLiquidationPenaltyRate,
        @JsonProperty("pre_liquidation_penalty_basis") String preLiquidationPenaltyBasis,
        @JsonProperty("pre_liquidation_penalty_domain") String preLiquidationPenaltyDomain,
        @JsonProperty("allow_premature_withdrawal_yesno") String allowPrematureWithdrawalYesno,
        @JsonProperty("withdrawal_penalty_rate") Double withdrawalPenaltyRate,
        @JsonProperty("withdrawal_penalty_basis") String withdrawalPenaltyBasis,
        @JsonProperty("withdrawal_penalty_domain") String withdrawalPenaltyDomain,
        @JsonProperty("in_use_yesno") String inUseYesno,
        @JsonProperty("meta_data") Object metaData
) {
}