package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.requests.EthicalEarningsCreateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.EthicalEarningsLiquidateRequest;
import org.meristem.oneapp.wealthservice.domains.requests.EthicalEarningsPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsCreateResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsLiquidateResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsListResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsPreviewResponse;
import org.meristem.oneapp.wealthservice.domains.responses.EthicalEarningsTransactionResponse;

import java.util.List;

public interface IEthicalEarningsService {
    EthicalEarningsPreviewResponse preview(EthicalEarningsPreviewRequest request);
    EthicalEarningsCreateResponse create(EthicalEarningsCreateRequest request);
    List<EthicalEarningsListResponse> getEthicalEarnings();
    List<EthicalEarningsTransactionResponse> getTransactions(String placementId);
    EthicalEarningsLiquidateResponse liquidate(EthicalEarningsLiquidateRequest request);
}