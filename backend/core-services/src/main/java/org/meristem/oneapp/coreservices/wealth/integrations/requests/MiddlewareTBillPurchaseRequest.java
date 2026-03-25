package org.meristem.oneapp.coreservices.wealth.integrations.requests;

public record MiddlewareTBillPurchaseRequest(String instrumentId,
                                   Double amount,
                                   String customerId) {
}
