package org.meristem.oneapp.wealthservice.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositCreateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositLiquidateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.responses.FixedDepositCreateResponse;
import org.meristem.oneapp.wealthservice.domains.responses.FixedDepositLiquidateResponse;
import org.meristem.oneapp.wealthservice.domains.responses.FixedDepositPreviewResponse;
import org.meristem.oneapp.wealthservice.domains.responses.FixedDepositResponse;
import org.meristem.oneapp.wealthservice.domains.responses.FixedDepositTransactionResponse;
import org.meristem.oneapp.wealthservice.integrations.MiddleWareClient;


import org.meristem.oneapp.wealthservice.integrations.requests.MiddlewarePlacementCreateFixedDepositRequest;
import org.meristem.oneapp.wealthservice.integrations.requests.MiddlewarePlacementLiquidateRequest;
import org.meristem.oneapp.wealthservice.integrations.requests.MiddlewarePlacementPreviewRequest;
import org.meristem.oneapp.wealthservice.integrations.responses.MiddlewarePlacementCreateFixedDepositResponse;
import org.meristem.oneapp.wealthservice.integrations.responses.MiddlewarePlacementLiquidateResponse;
import org.meristem.oneapp.wealthservice.integrations.responses.MiddlewarePlacementRateCalculateResponse;


import org.meristem.oneapp.wealthservice.mappers.MiddlewareMapper;
import org.meristem.oneapp.wealthservice.services.IFixedDepositService;
import org.meristem.oneapp.wealthservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FixedDepositService implements IFixedDepositService {

    private final MiddleWareClient middleWareClient;
    private final MiddlewareMapper middlewareMapper = MiddlewareMapper.INSTANCE;

    @Override
    public FixedDepositPreviewResponse preview(FixedDepositPreviewRequest request) {
        MiddlewarePlacementPreviewRequest middlewareRequest = new MiddlewarePlacementPreviewRequest(
                request.fundId(),
                request.productId(),
                request.effectiveDate(),
                request.amount(),
                request.tenorDays()
        );
        //todo: carter for reInvestmentPercentage
        MiddlewarePlacementRateCalculateResponse response = middleWareClient.calculatePlacementRate(middlewareRequest).data();
        return new FixedDepositPreviewResponse(
                response.matures(),
                response.tenor(),
                response.maturity(),
                response.rate(),
                response.netInterest(),
                response.grossInterest(),
                request.reInvestmentPercentage()
        );
    }

    @Override
    public FixedDepositCreateResponse create(FixedDepositCreateRequest request) {

        String customerId = AppUtil.getLoggedInCustomerId();
        String reference = java.util.UUID.randomUUID().toString().replace("-", "");


        MiddlewarePlacementCreateFixedDepositRequest middlewareRequest = new MiddlewarePlacementCreateFixedDepositRequest(
                request.productId(),
                customerId,
                request.symplusAccountNo(),
                request.date(),
                request.tenorInDays(),
                request.amount(),
                request.rollover(),
                reference
        );
        MiddlewarePlacementCreateFixedDepositResponse response = middleWareClient.createFixedDeposit(middlewareRequest).data();
        return new FixedDepositCreateResponse(response.reference());
    }

    @Override
    public List<FixedDepositResponse> getFixedDeposits() {
        String customerId = AppUtil.getLoggedInCustomerId();
        return middleWareClient.getFixedDepositsByCustomer(customerId).data()
                .stream()
                .map(d -> new FixedDepositResponse(
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
                        d.totalTaxPaidAmount()
                ))
                .toList();
    }

    @Override
    public List<FixedDepositTransactionResponse> getTransactions(String placementId) {
        return middleWareClient.getFixedDepositTransactions(placementId).data()
                .stream()
                .map(t -> new FixedDepositTransactionResponse(
                        t.fundId(),
                        t.fundDescription(),
                        t.productId(),
                        t.productDescription(),
                        t.currencyId(),
                        t.currencyDescription(),
                        t.customerId(),
                        t.customerName(),
                        t.fundAccountId(),
                        t.effectiveDate(),
                        t.transactionDate(),
                        t.transactionDescription(),
                        t.transactionReference(),
                        t.transactionAmount(),
                        t.transactionType()
                ))
                .toList();
    }

    @Override
    public FixedDepositLiquidateResponse liquidate(FixedDepositLiquidateRequest request) {
        String reference = java.util.UUID.randomUUID().toString().replace("-", "");
        MiddlewarePlacementLiquidateRequest middlewareRequest = new MiddlewarePlacementLiquidateRequest(
                request.placementId(),
                reference,
                request.date()
        );
        MiddlewarePlacementLiquidateResponse response = middleWareClient.liquidateFixedDeposit(middlewareRequest).data();
        return new FixedDepositLiquidateResponse(response.reference());
    }
}