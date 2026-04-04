package org.meristem.oneapp.wealthservice.services.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.domains.responses.InvestmentProductWithPlansResponse;
import org.meristem.oneapp.wealthservice.models.InvestmentPlanSettings;
import org.meristem.oneapp.wealthservice.models.InvestmentPlans;
import org.meristem.oneapp.wealthservice.models.InvestmentProducts;
import org.meristem.oneapp.wealthservice.repositories.InvestmentPlanSettingsRepository;
import org.meristem.oneapp.wealthservice.repositories.InvestmentPlansRepository;
import org.meristem.oneapp.wealthservice.repositories.InvestmentProductsRepository;
import org.meristem.oneapp.wealthservice.services.IInvestmentPlansService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InvestmentPlansService implements IInvestmentPlansService {

    private final InvestmentProductsRepository investmentProductsRepository;
    private final InvestmentPlansRepository investmentPlansRepository;
    private final InvestmentPlanSettingsRepository investmentPlanSettingsRepository;
    private final ObjectMapper objectMapper;

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
                        plan.getCoreProductId(),
                        plan.getCoreFundId(),
                        plan.getSlug(),
                        plan.getShortName(),
                        plan.getDescription(),
                        plan.getVideoUrl(),
                        plan.getPosition(),
                        plan.getIsActive(),
                        loadAboutDescription(plan.getUuid()),
                        loadAboutHighlights(plan.getUuid()),
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

    @Override
    public InvestmentProductWithPlansResponse.PlanResponse getPlanByUuid(String uuid) {
        InvestmentPlans plan = investmentPlansRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + uuid));

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

        return new InvestmentProductWithPlansResponse.PlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getUuid(),
                plan.getCoreProductId(),
                plan.getCoreFundId(),
                plan.getSlug(),
                plan.getShortName(),
                plan.getDescription(),
                plan.getVideoUrl(),
                plan.getPosition(),
                plan.getIsActive(),
                loadAboutDescription(plan.getUuid()),
                loadAboutHighlights(plan.getUuid()),
                settingsResponse
        );
    }

    private InvestmentProductWithPlansResponse.About loadAbout(String uuid) {
        try {
            ClassPathResource resource = new ClassPathResource("investment-plan-details.json");
            List<Map<String, Object>> details = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );
            return details.stream()
                    .filter(d -> uuid.equals(d.get("uuid")))
                    .findFirst()
                    .map(d -> objectMapper.convertValue(d.get("about"),
                            InvestmentProductWithPlansResponse.About.class))
                    .orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    private String loadAboutDescription(String uuid) {
        InvestmentProductWithPlansResponse.About about = loadAbout(uuid);
        return about != null ? about.description() : null;
    }

    private List<String> loadAboutHighlights(String uuid) {
        InvestmentProductWithPlansResponse.About about = loadAbout(uuid);
        return about != null ? about.highlights() : null;
    }
}