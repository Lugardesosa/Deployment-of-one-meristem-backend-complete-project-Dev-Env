package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.requests.PortfolioStatementRequest;
import org.meristem.oneapp.wealthservice.domains.responses.PortfolioActivityResponse;
import org.meristem.oneapp.wealthservice.domains.responses.PortfolioAssetClassBreakdownResponse;
import org.meristem.oneapp.wealthservice.domains.responses.PortfolioStatementResponse;

import java.util.List;

public interface IPortfolioService {

    List<PortfolioActivityResponse> getRecentPortfolioActivity(String customerId);

    List<PortfolioAssetClassBreakdownResponse> getPortfolioBreakdown(String customerId);

    PortfolioStatementResponse requestPortfolioStatement(PortfolioStatementRequest request);
}
