package org.meristem.oneapp.coreservices.wealth.services;

import org.meristem.oneapp.coreservices.wealth.domains.responses.RateQuoteResponse;

import java.util.List;

public interface IRateService {

    List<RateQuoteResponse> getTodayRates();
}
