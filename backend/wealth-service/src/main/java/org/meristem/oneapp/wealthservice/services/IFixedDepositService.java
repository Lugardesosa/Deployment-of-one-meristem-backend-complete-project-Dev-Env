package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositCreateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositLiquidateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.FixedDepositPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.responses.*;

import java.util.List;

public interface IFixedDepositService {
    FixedDepositPreviewResponse preview(FixedDepositPreviewRequest request);
    FixedDepositCreateResponse create(FixedDepositCreateRequest request);
    List<FixedDepositResponse> getFixedDeposits();
    List<FixedDepositTransactionResponse> getTransactions(String placementId);
    FixedDepositLiquidateResponse liquidate(FixedDepositLiquidateRequest request);
}