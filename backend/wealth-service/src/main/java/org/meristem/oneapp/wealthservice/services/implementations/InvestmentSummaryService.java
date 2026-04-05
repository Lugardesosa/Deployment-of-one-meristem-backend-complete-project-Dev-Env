package org.meristem.oneapp.wealthservice.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.domains.responses.InvestmentSummaryResponse;
import org.meristem.oneapp.wealthservice.integrations.MiddleWareClient;
import org.meristem.oneapp.wealthservice.integrations.responses.MiddlewareFixedDepositResponse;
import org.meristem.oneapp.wealthservice.models.InvestmentPlanSettings;
import org.meristem.oneapp.wealthservice.models.InvestmentPlans;
import org.meristem.oneapp.wealthservice.repositories.InvestmentPlanSettingsRepository;
import org.meristem.oneapp.wealthservice.repositories.InvestmentPlansRepository;
import org.meristem.oneapp.wealthservice.services.IInvestmentSummaryService;
import org.meristem.oneapp.wealthservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvestmentSummaryService implements IInvestmentSummaryService {

    private final MiddleWareClient middleWareClient;
    private final InvestmentPlansRepository investmentPlansRepository;
    private final InvestmentPlanSettingsRepository investmentPlanSettingsRepository;


    @Override
    public List<InvestmentSummaryResponse> getAllInvestments() {
        String customerId = AppUtil.getLoggedInCustomerId();
        List<InvestmentSummaryResponse> result = new ArrayList<>();

        try {
            List<MiddlewareFixedDepositResponse> allPlacements = middleWareClient
                    .getFixedDepositsByCustomer(customerId).data();

            allPlacements.forEach(d -> {
                String type = resolveType(d.productId());
                InvestmentSummaryResponse.Actions actions = resolveActions(d.productId());

                result.add(new InvestmentSummaryResponse(
                        type,
                        d.accountType(),
                        d.accruedInterestAmount(),
                        d.bookOfAccountId(),
                        d.currencyDescription(),
                        d.currencyId(),
                        d.currentBalanceAmount(),
                        d.customerId(),
                        d.customerName(),
                        d.dateClosed(),
                        d.daysToMaturity(),
                        d.debitAccountDescription(),
                        d.debitAccountId(),
                        d.effectiveDate(),
                        d.expectedGrossInterestAmount(),
                        d.expectedNetInterestAmount(),
                        d.fundDescription(),
                        d.fundId(),
                        d.interestRate(),
                        d.investmentAdditionAmount(),
                        d.investmentAmount(),
                        d.isClosedYesNo(),
                        d.lastAccruedDate(),
                        d.maturityDate(),
                        d.maturityMandate(),
                        d.placementId(),
                        d.preLiquidationPenaltyRate(),
                        d.productDescription(),
                        d.productId(),
                        d.rolloverSequence(),
                        d.status(),
                        d.tenorDays(),
                        d.totalInterestPaidAmount(),
                        d.totalTaxPaidAmount(),
                        actions
                ));
            });
        } catch (Exception e) {
            // return empty if middleware fails
        }

        return result;
    }

    @Override
    public InvestmentSummaryResponse getInvestmentByPlacementId(String placementId) {
        return getAllInvestments().stream()
                .filter(i -> placementId.equals(i.placementId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Investment not found: " + placementId));
    }


    private String resolveType(String productId) {
        return switch (productId) {
            case "EEP" -> "ETHICAL_EARNINGS";
            case "FIVP" -> "FIXED_DEPOSIT";
            default -> "INVESTMENT";
        };
    }

    private InvestmentSummaryResponse.Actions resolveActions(String productId) {
        try {
            Optional<InvestmentPlans> plan = investmentPlansRepository.findByCoreProductId(productId);
            if (plan.isEmpty()) return defaultActions();

            Optional<InvestmentPlanSettings> settings = investmentPlanSettingsRepository
                    .findByInvestmentPlanId(plan.get().getId());
            if (settings.isEmpty()) return defaultActions();

            InvestmentPlanSettings s = settings.get();
            return new InvestmentSummaryResponse.Actions(
                    s.getCanWithdrawActive(),
                    s.getCanSetupRecurringDebits(),
                    s.getCanFundActive(),
                    s.getPenaltyOnEarlyWithdrawal(),
                    s.getPenaltyOnInterest(),
                    s.getFundWithOtherInvestments()
            );
        } catch (Exception e) {
            return defaultActions();
        }
    }
    private InvestmentSummaryResponse.Actions defaultActions() {
        return new InvestmentSummaryResponse.Actions(
                false, false, false, false, false, false
        );
    }

}