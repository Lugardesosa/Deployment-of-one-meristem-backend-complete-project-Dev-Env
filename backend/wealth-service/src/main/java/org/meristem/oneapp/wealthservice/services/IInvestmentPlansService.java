package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.responses.InvestmentProductWithPlansResponse;

import java.util.List;

public interface IInvestmentPlansService {
    List<InvestmentProductWithPlansResponse> getAllProductsWithPlans();
}