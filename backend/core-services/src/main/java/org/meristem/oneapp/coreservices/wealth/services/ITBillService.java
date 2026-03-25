package org.meristem.oneapp.coreservices.wealth.services;

import org.meristem.oneapp.coreservices.wealth.domains.requests.TBillPreviewRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.TBillPurchaseRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.TBillSellPreviewRequest;
import org.meristem.oneapp.coreservices.wealth.domains.requests.TBillSellRequest;
import org.meristem.oneapp.coreservices.wealth.domains.responses.*;

import java.util.List;

public interface ITBillService {

    List<TBillProductResponse> getTBillProducts();

    TBillRateResponse getTBillRate(String instrumentId);

    TBillPreviewResponse previewTBillPurchase(TBillPreviewRequest request);

    TBillPurchaseResponse purchaseTBill(TBillPurchaseRequest request);

    List<ActiveTBillResponse> getActiveTBills(String customerId);

    TBillSellPreviewResponse previewTBillSale(TBillSellPreviewRequest request);

    TBillSellResponse sellTBill(TBillSellRequest request);
}
