package org.meristem.oneapp.wealthservice.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Table("investment_plan_settings")
public class InvestmentPlanSettings extends BaseModel<String>{

    private Long investmentPlanId;

    private BigDecimal interest;
    private BigDecimal effectiveYield;
    private BigDecimal grossYield;

    private BigDecimal processingFeePercentage;

    private Integer interestPeriod;

    private Long minimumInvestment;
    private Integer minimumTenureDays;
    private Integer maximumTenureDays;

    private String rateOfReturn;
    private String investmentDenomination;
    private String riskLevel;

    private Long minimumRecurringAmount;

    private String returnsType;
    private String computeType;

    private BigDecimal bid;
    private BigDecimal offer;

    private BigDecimal minimumUnits;
    private BigDecimal minimumRecurringUnits;

    private Boolean canWithdrawActive;
    private Boolean canSetupRecurringDebits;
    private Boolean canFundActive;

    private Boolean penaltyOnEarlyWithdrawal;
    private Boolean penaltyOnInterest;
    private BigDecimal penaltyPercentage;

    private Boolean fundWithOtherInvestments;

    private Long minimumTopupAmount;
    private BigDecimal minimumTopupUnits;


    private Integer tenor;
    private String offerOpenDate;
    private String offerCloseDate;
    private String investmentStartDate;
    private String investmentEndDate;
    private String daysArray;


    @Builder
    public InvestmentPlanSettings(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Integer status, Long investmentPlanId, BigDecimal interest, BigDecimal effectiveYield, BigDecimal grossYield, BigDecimal processingFeePercentage, Integer interestPeriod, Long minimumInvestment, Integer minimumTenureDays, Integer maximumTenureDays, String rateOfReturn, String investmentDenomination, String riskLevel, Long minimumRecurringAmount, String returnsType, String computeType, BigDecimal bid, BigDecimal offer, BigDecimal minimumUnits, BigDecimal minimumRecurringUnits, Boolean canWithdrawActive, Boolean canSetupRecurringDebits, Boolean canFundActive, Boolean penaltyOnEarlyWithdrawal, Boolean penaltyOnInterest, BigDecimal penaltyPercentage, Boolean fundWithOtherInvestments, Long minimumTopupAmount, BigDecimal minimumTopupUnits,String daysArray) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.investmentPlanId = investmentPlanId;
        this.interest = interest;
        this.effectiveYield = effectiveYield;
        this.grossYield = grossYield;
        this.processingFeePercentage = processingFeePercentage;
        this.interestPeriod = interestPeriod;
        this.minimumInvestment = minimumInvestment;
        this.minimumTenureDays = minimumTenureDays;
        this.maximumTenureDays = maximumTenureDays;
        this.rateOfReturn = rateOfReturn;
        this.investmentDenomination = investmentDenomination;
        this.riskLevel = riskLevel;
        this.minimumRecurringAmount = minimumRecurringAmount;
        this.returnsType = returnsType;
        this.computeType = computeType;
        this.bid = bid;
        this.offer = offer;
        this.minimumUnits = minimumUnits;
        this.minimumRecurringUnits = minimumRecurringUnits;
        this.canWithdrawActive = canWithdrawActive;
        this.canSetupRecurringDebits = canSetupRecurringDebits;
        this.canFundActive = canFundActive;
        this.penaltyOnEarlyWithdrawal = penaltyOnEarlyWithdrawal;
        this.penaltyOnInterest = penaltyOnInterest;
        this.penaltyPercentage = penaltyPercentage;
        this.fundWithOtherInvestments = fundWithOtherInvestments;
        this.minimumTopupAmount = minimumTopupAmount;
        this.minimumTopupUnits = minimumTopupUnits;
        this.daysArray = daysArray;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentPlanSettings that = (InvestmentPlanSettings) o;
        return Objects.equals(getInvestmentPlanId(), that.getInvestmentPlanId()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvestmentPlanId(), getId());
    }
}
