package org.meristem.oneapp.coreservices.wealth.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservices.wealth.domains.requests.TBillPreviewRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.TBillPurchaseRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.TBillSellPreviewRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.TBillSellRequest;
import org.meristem.oneapp.coreservices.wealth.domains.responses.*;
import org.meristem.oneapp.coreservices.wealth.integrations.MiddleWareClient;
import org.meristem.oneapp.coreservices.wealth.mappers.MiddlewareMapper;
import org.meristem.oneapp.coreservices.wealth.services.ITBillService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TBillService implements ITBillService {

    private final MiddleWareClient middleWareClient;
    private final MiddlewareMapper middlewareMapper = MiddlewareMapper.INSTANCE;

    @Override
    public List<TBillProductResponse> getTBillProducts() {
        return middlewareMapper.middlewareTBillProductResponseToTBillProductResponse(middleWareClient.getTBillProducts().data());
    }

    @Override
    public TBillRateResponse getTBillRate(String instrumentId) {
        return middlewareMapper.middlewareTBillRateResponseToTBillRateResponse(middleWareClient.getTBillRate(instrumentId).data());
    }

    @Override
    public TBillPreviewResponse previewTBillPurchase(TBillPreviewRequest request) {
        return middlewareMapper.middlewareTBillPreviewResponseToTBillPreviewResponse(
                middleWareClient.previewTBillPurchase(middlewareMapper.tBillPreviewRequestToMiddlewareTBillPreviewRequest(request)).data()
        );
    }

    @Override
    public TBillPurchaseResponse purchaseTBill(TBillPurchaseRequest request) {
        return middlewareMapper.middlewareTBillPurchaseResponseToTBillPurchaseResponse(
                middleWareClient.purchaseTBill(middlewareMapper.tBillPurchaseRequestToMiddlewareTBillPurchaseRequest(request)).data()
        );
    }

    @Override
    public List<ActiveTBillResponse> getActiveTBills(String customerId) {
        return middlewareMapper.middlewareActiveTBillResponseToActiveTBillResponse(middleWareClient.getActiveTBills(customerId).data());
    }

    @Override
    public TBillSellPreviewResponse previewTBillSale(TBillSellPreviewRequest request) {
        return middlewareMapper.middlewareTBillSellPreviewToTBillSellPreviewResponse(
                middleWareClient.previewTBillSale(middlewareMapper.tBillSellPreviewRequestToMiddlewareTBillSellPreviewRequest(request)).data()
        );
    }

    @Override
    public TBillSellResponse sellTBill(TBillSellRequest request) {
        return middlewareMapper.middlewareTBillSellResponseToTBillSellResponse(
                middleWareClient.sellTBill(middlewareMapper.tBillSellRequestToMiddlewareTBillSellRequest(request)).data()
        );
    }
}
