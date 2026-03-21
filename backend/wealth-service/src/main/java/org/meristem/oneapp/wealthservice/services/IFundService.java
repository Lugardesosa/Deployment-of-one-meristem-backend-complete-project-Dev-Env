package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.requests.FundRedemptionRequest;
import org.meristem.oneapp.wealthservice.domains.requests.FundSubscriptionRequest;
import org.meristem.oneapp.wealthservice.domains.responses.*;

import java.util.List;

public interface IFundService {

    List<FundResponse> getAllFundTypes();

    FundDetailsResponse getFundById(String fundId);

    FundSubscriptionResponse subscribeToFund(String fundId,
                                                          FundSubscriptionRequest request);

    FundRedemptionResponse redeemFund(String fundId,
                                                   FundRedemptionRequest request);

    List<FundAccountsResponse> getFundAccounts(String customerId);

    List<PortfolioResponse> getPortfolio(String customerId);

    List<FundHistoryResponse> getFundHistory(String fundId);

    List<FundStatementResponse> getFundsStatements();
}
