package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.responses.RateQuoteResponse;

import java.util.List;

public interface IRateService {

    List<RateQuoteResponse> getTodayRates();
}
