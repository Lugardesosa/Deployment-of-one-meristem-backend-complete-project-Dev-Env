package org.meristem.oneapp.wealthservice.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.domains.requests.EthicalEarningsCreateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.EthicalEarningsLiquidateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.EthicalEarningsPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsCreateResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsLiquidateResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsListResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsPreviewResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsTransactionResponse;
import org.meristem.oneapp.wealthservice.integrations.MiddleWareClient;
import org.meristem.oneapp.wealthservice.integrations.requests.MiddlewarePlacementCreateFixedDepositRequest;
import org.meristem.oneapp.wealthservice.integrations.requests.MiddlewarePlacementLiquidateRequest;
import org.meristem.oneapp.wealthservice.integrations.requests.MiddlewarePlacementPreviewRequest;
import org.meristem.oneapp.wealthservice.integrations.responses.MiddlewarePlacementCreateFixedDepositResponse;
import org.meristem.oneapp.wealthservice.integrations.responses.MiddlewarePlacementLiquidateResponse;
import org.meristem.oneapp.wealthservice.integrations.responses.MiddlewarePlacementRateCalculateResponse;
import org.meristem.oneapp.wealthservice.services.IEthicalEarningsService;
import org.meristem.oneapp.wealthservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EthicalEarningsService implements IEthicalEarningsService {

    private final MiddleWareClient middleWareClient;

    @Override
    public EthicalEarningsPreviewResponse preview(EthicalEarningsPreviewRequest request) {
        MiddlewarePlacementPreviewRequest middlewareRequest = new MiddlewarePlacementPreviewRequest(
                request.fundId(),
                request.productId(),
                request.effectiveDate(),
                request.amount(),
                request.tenorDays()
        );
        MiddlewarePlacementRateCalculateResponse response = middleWareClient.calculatePlacementRate(middlewareRequest).data();
        return new EthicalEarningsPreviewResponse(
                response.matures(),
                response.tenor(),
                response.maturity(),
                response.rate(),
                response.netInterest(),
                response.grossInterest()
        );
    }

    @Override
    public EthicalEarningsCreateResponse create(EthicalEarningsCreateRequest request) {
        String customerId = AppUtil.getLoggedInCustomerId();
        String reference = java.util.UUID.randomUUID().toString().replace("-", "");
        String investmentDate = "2025-12-31"; //todo:remove in future once the date has been configured on core

        MiddlewarePlacementCreateFixedDepositRequest middlewareRequest = new MiddlewarePlacementCreateFixedDepositRequest(
                request.productId(),
                customerId,
                request.symplusAccountNo(),
                investmentDate,
                request.tenorInDays(),
                request.amount(),
                request.rollover(),
                reference
        );
        MiddlewarePlacementCreateFixedDepositResponse response = middleWareClient.createFixedDeposit(middlewareRequest).data();
        return new EthicalEarningsCreateResponse(response.reference());
    }

    @Override
    public List<EthicalEarningsListResponse> getEthicalEarnings() {
        String customerId = AppUtil.getLoggedInCustomerId();
        return middleWareClient.getFixedDepositsByCustomer(customerId).data()
                .stream()
                .filter(d -> "EEP".equals(d.productId()))
                .map(d -> new EthicalEarningsListResponse(
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
    public List<EthicalEarningsTransactionResponse> getTransactions(String placementId) {
        return middleWareClient.getFixedDepositTransactions(placementId).data()
                .stream()
                .map(t -> new EthicalEarningsTransactionResponse(
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
    public EthicalEarningsLiquidateResponse liquidate(EthicalEarningsLiquidateRequest request) {
        String reference = java.util.UUID.randomUUID().toString().replace("-", "");
        MiddlewarePlacementLiquidateRequest middlewareRequest = new MiddlewarePlacementLiquidateRequest(
                request.placementId(),
                reference,
                request.date()
        );
        MiddlewarePlacementLiquidateResponse response = middleWareClient.liquidateFixedDeposit(middlewareRequest).data();
        return new EthicalEarningsLiquidateResponse(response.reference());
    }
}