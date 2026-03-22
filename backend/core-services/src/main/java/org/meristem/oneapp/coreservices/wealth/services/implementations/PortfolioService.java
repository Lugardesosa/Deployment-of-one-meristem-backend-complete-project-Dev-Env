package org.meristem.oneapp.coreservices.wealth.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservices.wealth.domains.requests.PortfolioStatementRequest;
import org.meristem.oneapp.coreservices.wealth.domains.responses.PortfolioActivityResponse;
import org.meristem.oneapp.coreservices.wealth.domains.responses.PortfolioAssetClassBreakdownResponse;
import org.meristem.oneapp.coreservices.wealth.domains.responses.PortfolioStatementResponse;
import org.meristem.oneapp.coreservices.wealth.integrations.MiddleWareClient;
import org.meristem.oneapp.coreservices.wealth.mappers.MiddlewareMapper;
import org.meristem.oneapp.coreservices.wealth.services.IPortfolioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService implements IPortfolioService {

    private final MiddleWareClient middleWareClient;
    private final MiddlewareMapper middlewareMapper = MiddlewareMapper.INSTANCE;

    @Override
    public List<PortfolioActivityResponse> getRecentPortfolioActivity(String customerId) {
        return middlewareMapper.middlewarePortfolioActivityResponseToPortfolioActivityResponse(middleWareClient.getRecentPortfolioActivity(customerId).data());
    }

    @Override
    public List<PortfolioAssetClassBreakdownResponse> getPortfolioBreakdown(String customerId) {
        return middlewareMapper.middlewarePortfolioAssetToPortfolioAssetClassBreakdownResponse(middleWareClient.getPortfolioBreakdown(customerId).data());
    }

    @Override
    public PortfolioStatementResponse requestPortfolioStatement(PortfolioStatementRequest request) {
        return middlewareMapper.middlewarePortfolioStatementToPortfolioStatementResponse(
                middleWareClient.requestPortfolioStatement(middlewareMapper.portfolioStatementRequestToMiddlewarePortfolioStatementRequest(request)).data()
        );
    }
}
