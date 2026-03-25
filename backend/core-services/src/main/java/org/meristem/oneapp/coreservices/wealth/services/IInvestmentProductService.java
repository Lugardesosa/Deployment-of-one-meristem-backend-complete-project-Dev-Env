package org.meristem.oneapp.coreservices.wealth.services;

import org.meristem.oneapp.coreservices.wealth.domains.responses.InvestmentProductResponse;

import java.util.List;

public interface IInvestmentProductService {

    List<InvestmentProductResponse> getInvestmentProducts();
}
