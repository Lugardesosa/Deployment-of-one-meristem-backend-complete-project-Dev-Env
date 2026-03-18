package org.meristem.oneapp.wealthservice.services;

import org.meristem.oneapp.wealthservice.domains.requests.TBillPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.requests.TBillPurchaseRequest;
import org.meristem.oneapp.wealthservice.domains.requests.TBillSellPreviewRequest;
import org.meristem.oneapp.wealthservice.domains.requests.TBillSellRequest;
import org.meristem.oneapp.wealthservice.domains.responses.*;

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
