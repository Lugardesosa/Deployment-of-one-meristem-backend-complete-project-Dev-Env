package org.meristem.oneapp.coreservices.wealth.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservices.wealth.domains.responses.InvestmentProductResponse;
import org.meristem.oneapp.coreservices.wealth.integrations.MiddleWareClient;
import org.meristem.oneapp.coreservices.wealth.mappers.MiddlewareMapper;
import org.meristem.oneapp.coreservices.wealth.services.IInvestmentProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestmentProductService implements IInvestmentProductService {

    private final MiddleWareClient middleWareClient;
    private final MiddlewareMapper middlewareMapper = MiddlewareMapper.INSTANCE;

    @Override
    public List<InvestmentProductResponse> getInvestmentProducts() {
        return middlewareMapper.middlewareInvestmentProductResponseToInvestmentProductResponse(middleWareClient.getInvestmentProducts().data());
    }
}
