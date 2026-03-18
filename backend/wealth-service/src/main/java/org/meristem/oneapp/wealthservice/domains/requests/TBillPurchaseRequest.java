package org.meristem.oneapp.wealthservice.domains.requests;

public record TBillPurchaseRequest(String instrumentId,
                                   Double amount,
                                   String customerId) {
}
