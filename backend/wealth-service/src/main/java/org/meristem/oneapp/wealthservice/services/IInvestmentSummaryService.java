package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.responses.InvestmentSummaryResponse;

import java.util.List;

public interface IInvestmentSummaryService {
    List<InvestmentSummaryResponse> getAllInvestments();
    InvestmentSummaryResponse getInvestmentByPlacementId(String placementId);
}