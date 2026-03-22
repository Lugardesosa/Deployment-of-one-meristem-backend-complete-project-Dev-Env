package org.meristem.oneapp.coreservices.wealth.domains.requests;

public record TBillPurchaseRequest(String instrumentId,
                                   Double amount,
                                   String customerId) {
}
