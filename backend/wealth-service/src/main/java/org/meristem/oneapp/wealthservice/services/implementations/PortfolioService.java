package org.meristem.oneapp.wealthservice.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.wealthservice.domains.requests.PortfolioStatementRequest;
import org.meristem.oneapp.wealthservice.domains.responses.PortfolioActivityResponse;
import org.meristem.oneapp.wealthservice.domains.responses.PortfolioAssetClassBreakdownResponse;
import org.meristem.oneapp.wealthservice.domains.responses.PortfolioStatementResponse;
import org.meristem.oneapp.wealthservice.integrations.MiddleWareClient;
import org.meristem.oneapp.wealthservice.mappers.MiddlewareMapper;
import org.meristem.oneapp.wealthservice.services.IPortfolioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
