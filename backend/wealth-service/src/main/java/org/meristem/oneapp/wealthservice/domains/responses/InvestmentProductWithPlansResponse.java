package org.meristem.oneapp.wealthservice.domains.responses;

import java.math.BigDecimal;
import java.util.List;

public record InvestmentProductWithPlansResponse(
        Long id,
        String name,
        String uuid,
        String slug,
        String supportedCurrency,
        String description,
        Integer position,
        Boolean isActive,
        List<PlanResponse> plans
) {
    public record PlanResponse(
            Long id,
            String name,
            String uuid,
            String slug,
            String shortName,
            String description,
            String videoUrl,
            Integer position,
            Boolean isActive,
            String aboutDescription,
            List<String> aboutHighlights,
            PlanSettingsResponse settings
    ) {}

    public record PlanSettingsResponse(
            Long id,
            BigDecimal interest,
            BigDecimal effectiveYield,
            BigDecimal grossYield,
            BigDecimal processingFeePercentage,
            Integer interestPeriod,
            Long minimumInvestment,
            Integer minimumTenureDays,
            Integer maximumTenureDays,
            String rateOfReturn,
            String investmentDenomination,
            String riskLevel,
            Long minimumRecurringAmount,
            String returnsType,
            String computeType,
            BigDecimal bid,
            BigDecimal offer,
            BigDecimal minimumUnits,
            BigDecimal minimumRecurringUnits,
            Boolean canWithdrawActive,
            Boolean canSetupRecurringDebits,
            Boolean canFundActive,
            Boolean penaltyOnEarlyWithdrawal,
            Boolean penaltyOnInterest,
            BigDecimal penaltyPercentage,
            Boolean fundWithOtherInvestments,
            Long minimumTopupAmount,
            BigDecimal minimumTopupUnits,
            String offerOpenDate,
            String offerCloseDate,
            String investmentStartDate,
            String investmentEndDate
    ) {}

    public record About(String description, List<String> highlights) {}
}