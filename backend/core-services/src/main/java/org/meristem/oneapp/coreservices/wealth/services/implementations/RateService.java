package org.meristem.oneapp.coreservices.wealth.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservices.wealth.domains.responses.RateQuoteResponse;
import org.meristem.oneapp.coreservices.wealth.integrations.MiddleWareClient;
import org.meristem.oneapp.coreservices.wealth.mappers.MiddlewareMapper;
import org.meristem.oneapp.coreservices.wealth.services.IRateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RateService implements IRateService {

    private final MiddleWareClient middleWareClient;
    private final MiddlewareMapper middlewareMapper = MiddlewareMapper.INSTANCE;

    @Override
    public List<RateQuoteResponse> getTodayRates() {
        return middlewareMapper.middlewareRateQuoteResponseToRateQuoteResponse(middleWareClient.getTodayRates().data());
    }
}
