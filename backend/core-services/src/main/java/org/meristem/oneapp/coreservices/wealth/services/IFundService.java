package org.meristem.oneapp.coreservices.wealth.services;

import org.meristem.oneapp.coreservices.wealth.domains.requests.FundRedemptionRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.FundSubscriptionRequest;
import org.meristem.oneapp.coreservices.wealth.domains.responses.*;

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
