package org.meristem.oneapp.wealthservice.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.domains.requests.FundRedemptionRequest;
import org.meristem.oneapp.wealthservice.domains.requests.FundSubscriptionRequest;
import org.meristem.oneapp.wealthservice.domains.responses.*;
import org.meristem.oneapp.wealthservice.integrations.MiddleWareClient;
import org.meristem.oneapp.wealthservice.mappers.MiddlewareMapper;
import org.meristem.oneapp.wealthservice.services.IFundService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundService implements IFundService {

    private final MiddleWareClient middleWareClient;
    private final MiddlewareMapper middlewareMapper = MiddlewareMapper.INSTANCE;

    @Override
    public List<FundResponse> getAllFundTypes() {
        return middlewareMapper.middlewareFundResponseToFundResponse(middleWareClient.getAllFundTypes().data());
    }

    @Override
    public FundDetailsResponse getFundById(String fundId) {
        return middlewareMapper.middlewareFundDetailsResponseToFundDetailsResponse(middleWareClient.getFundById(fundId).data());
    }

    @Override
    public FundSubscriptionResponse subscribeToFund(String fundId, FundSubscriptionRequest request) {
        return middlewareMapper.middlewareFundSubscriptionResponseToFundSubscriptionResponse(
                middleWareClient.subscribeToFund(fundId, middlewareMapper.fundSubscriptionRequestToMiddlewareFundSubscriptionRequest(request)).data()
        );
    }

    @Override
    public FundRedemptionResponse redeemFund(String fundId,
                                             FundRedemptionRequest request) {
        return middlewareMapper.middlewareFundRedemptionResponseToFundRedemptionResponse(
                middleWareClient.redeemFund(fundId, middlewareMapper.fundRedemptionRequestToMiddlewareFundRedemptionRequest(request)).data()
        );
    }

    @Override
    public List<FundAccountsResponse> getFundAccounts(String customerId) {
        return middlewareMapper.middlewareFundAccountsResponseToFundAccountsResponse(middleWareClient.getFundAccounts(customerId).data());
    }

    @Override
    public List<PortfolioResponse> getPortfolio(String customerId) {
        return middlewareMapper.middlewarePortfolioResponseToPortfolioResponse(middleWareClient.getPortfolio(customerId).data());
    }

    @Override
    public List<FundHistoryResponse> getFundHistory(String fundId) {
        return middlewareMapper.middlewareFundHistoryResponseToFundHistoryResponse(middleWareClient.getFundHistory(fundId).data());
    }

    @Override
    public List<FundStatementResponse> getFundsStatements() {
        return middlewareMapper.middlewareFundStatementResponseToFundStatementResponse(middleWareClient.getFundsStatements().data());
    }
}
