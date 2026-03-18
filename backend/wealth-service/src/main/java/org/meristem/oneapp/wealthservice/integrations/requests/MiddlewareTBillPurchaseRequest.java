package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewareTBillPurchaseRequest(String instrumentId,
                                   Double amount,
                                   String customerId) {
}
