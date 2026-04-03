package org.meristem.oneapp.wealthservice.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.domains.responses.InvestmentProductWithPlansResponse;
import org.meristem.oneapp.wealthservice.models.InvestmentPlanSettings;
import org.meristem.oneapp.wealthservice.models.InvestmentPlans;
import org.meristem.oneapp.wealthservice.models.InvestmentProducts;
import org.meristem.oneapp.wealthservice.repositories.InvestmentPlanSettingsRepository;
import org.meristem.oneapp.wealthservice.repositories.InvestmentPlansRepository;
import org.meristem.oneapp.wealthservice.repositories.InvestmentProductsRepository;
import org.meristem.oneapp.wealthservice.services.IInvestmentPlansService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestmentPlansService implements IInvestmentPlansService {

    private final InvestmentProductsRepository investmentProductsRepository;
    private final InvestmentPlansRepository investmentPlansRepository;
    private final InvestmentPlanSettingsRepository investmentPlanSettingsRepository;

    @Override
    public List<InvestmentProductWithPlansResponse> getAllProductsWithPlans() {
        List<InvestmentProducts> products = (List<InvestmentProducts>) investmentProductsRepository.findAll();
        List<InvestmentProductWithPlansResponse> result = new ArrayList<>();

        for (InvestmentProducts product : products) {
            List<InvestmentPlans> plans = investmentPlansRepository.findByInvestmentProductId(product.getId());
            List<InvestmentProductWithPlansResponse.PlanResponse> planResponses = new ArrayList<>();

            for (InvestmentPlans plan : plans) {
                InvestmentPlanSettings settings = investmentPlanSettingsRepository
                        .findByInvestmentPlanId(plan.getId())
                        .orElse(null);

                InvestmentProductWithPlansResponse.PlanSettingsResponse settingsResponse = settings == null ? null :
                        new InvestmentProductWithPlansResponse.PlanSettingsResponse(
                                settings.getId(),
                                settings.getInterest(),
                                settings.getEffectiveYield(),
                                settings.getGrossYield(),
                                settings.getProcessingFeePercentage(),
                                settings.getInterestPeriod(),
                                settings.getMinimumInvestment(),
                                settings.getMinimumTenureDays(),
                                settings.getMaximumTenureDays(),
                                settings.getRateOfReturn(),
                                settings.getInvestmentDenomination(),
                                settings.getRiskLevel(),
                                settings.getMinimumRecurringAmount(),
                                settings.getReturnsType(),
                                settings.getComputeType(),
                                settings.getBid(),
                                settings.getOffer(),
                                settings.getMinimumUnits(),
                                settings.getMinimumRecurringUnits(),
                                settings.getCanWithdrawActive(),
                                settings.getCanSetupRecurringDebits(),
                                settings.getCanFundActive(),
                                settings.getPenaltyOnEarlyWithdrawal(),
                                settings.getPenaltyOnInterest(),
                                settings.getPenaltyPercentage(),
                                settings.getFundWithOtherInvestments(),
                                settings.getMinimumTopupAmount(),
                                settings.getMinimumTopupUnits(),
                                settings.getOfferOpenDate(),
                                settings.getOfferCloseDate(),
                                settings.getInvestmentStartDate(),
                                settings.getInvestmentEndDate()
                        );

                planResponses.add(new InvestmentProductWithPlansResponse.PlanResponse(
                        plan.getId(),
                        plan.getName(),
                        plan.getUuid(),
                        plan.getSlug(),
                        plan.getShortName(),
                        plan.getDescription(),
                        plan.getVideoUrl(),
                        plan.getPosition(),
                        plan.getIsActive(),
                        settingsResponse
                ));
            }

            result.add(new InvestmentProductWithPlansResponse(
                    product.getId(),
                    product.getName(),
                    product.getUuid(),
                    product.getSlug(),
                    product.getSupportedCurrency(),
                    product.getDescription(),
                    product.getPosition(),
                    product.getIsActive(),
                    planResponses
            ));
        }

        return result;
    }
}