package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.responses.InvestmentProductResponse;

import java.util.List;

public interface IInvestmentProductService {

    List<InvestmentProductResponse> getInvestmentProducts();
}
