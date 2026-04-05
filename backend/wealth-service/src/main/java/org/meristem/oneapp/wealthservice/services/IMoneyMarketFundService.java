package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.requests.MoneyMarketFundPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.requests.MoneyMarketFundSubscribeRequest;
import org.meristem.oneapp.wealthservice.domains.responses.MoneyMarketFundPreviewResponse;
import org.meristem.oneapp.wealthservice.domains.responses.MoneyMarketFundSubscribeResponse;

public interface IMoneyMarketFundService {
    MoneyMarketFundPreviewResponse preview(MoneyMarketFundPreviewRequest request);
    MoneyMarketFundSubscribeResponse subscribe(MoneyMarketFundSubscribeRequest request);
}