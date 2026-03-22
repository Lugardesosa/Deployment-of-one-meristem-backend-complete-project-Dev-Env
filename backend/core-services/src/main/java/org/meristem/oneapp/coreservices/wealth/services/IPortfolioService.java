package org.meristem.oneapp.coreservices.wealth.services;

import org.meristem.oneapp.coreservices.wealth.domains.requests.PortfolioStatementRequest;
import org.meristem.oneapp.coreservices.wealth.domains.responses.PortfolioActivityResponse;
import org.meristem.oneapp.coreservices.wealth.domains.responses.PortfolioAssetClassBreakdownResponse;
import org.meristem.oneapp.coreservices.wealth.domains.responses.PortfolioStatementResponse;

import java.util.List;

public interface IPortfolioService {

    List<PortfolioActivityResponse> getRecentPortfolioActivity(String customerId);

    List<PortfolioAssetClassBreakdownResponse> getPortfolioBreakdown(String customerId);

    PortfolioStatementResponse requestPortfolioStatement(PortfolioStatementRequest request);
}
