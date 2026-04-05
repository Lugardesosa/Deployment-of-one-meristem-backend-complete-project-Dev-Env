package org.meristem.oneapp.wealthservice.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.domains.requests.MoneyMarketFundPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.requests.MoneyMarketFundSubscribeRequest;
import org.meristem.oneapp.wealthservice.domains.responses.MoneyMarketFundPreviewResponse;
import org.meristem.oneapp.wealthservice.domains.responses.MoneyMarketFundSubscribeResponse;
import org.meristem.oneapp.wealthservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.wealthservice.integrations.MiddleWareClient;
import org.meristem.oneapp.wealthservice.integrations.requests.MiddlewareFundSubscriptionRequest;
import org.meristem.oneapp.wealthservice.integrations.responses.MiddlewareFundSubscriptionResponse;
import org.meristem.oneapp.wealthservice.models.InvestmentPlanSettings;
import org.meristem.oneapp.wealthservice.repositories.InvestmentPlanSettingsRepository;
import org.meristem.oneapp.wealthservice.repositories.InvestmentPlansRepository;
import org.meristem.oneapp.wealthservice.services.IMoneyMarketFundService;
import org.meristem.oneapp.wealthservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MoneyMarketFundService implements IMoneyMarketFundService {

    private final MiddleWareClient middleWareClient;
    private final InvestmentPlansRepository investmentPlansRepository;
    private final InvestmentPlanSettingsRepository investmentPlanSettingsRepository;

    @Override
    public MoneyMarketFundPreviewResponse preview(MoneyMarketFundPreviewRequest request) {
        InvestmentPlanSettings settings = investmentPlansRepository.findByCoreFundId(request.fundId())
                .map(plan -> investmentPlanSettingsRepository.findByInvestmentPlanId(plan.getId())
                        .orElse(null))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fund not found: " + request.fundId(), "", ""));

        double processingFee = settings.getProcessingFeePercentage() != null
                ? request.amount() * settings.getProcessingFeePercentage().doubleValue() / 100
                : 0.0;

        return new MoneyMarketFundPreviewResponse(
                request.fundId(),
                request.amount(),
                LocalDate.now(),
                processingFee,
                settings.getRiskLevel(),
                settings.getReturnsType(),
                settings.getEffectiveYield(),
                settings.getGrossYield(),
                settings.getMinimumInvestment(),
                settings.getInvestmentDenomination()
        );
    }

    @Override
    public MoneyMarketFundSubscribeResponse subscribe(MoneyMarketFundSubscribeRequest request) {
        String customerId = AppUtil.getLoggedInCustomerId();
        String accountName = generateAccountName();


        MiddlewareFundSubscriptionRequest middlewareRequest = new MiddlewareFundSubscriptionRequest(
                customerId,
                accountName,
                request.amount()
        );

        MiddlewareFundSubscriptionResponse response = middleWareClient
                .subscribeToFund(request.fundId(), middlewareRequest).data();

        return new MoneyMarketFundSubscribeResponse(
                response.transactionId(),
                response.fundId(),
                response.customerId(),
                response.amount(),
                response.fundAccountNo(),
                response.status()
        );
    }

    private String generateAccountName() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(8);
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}