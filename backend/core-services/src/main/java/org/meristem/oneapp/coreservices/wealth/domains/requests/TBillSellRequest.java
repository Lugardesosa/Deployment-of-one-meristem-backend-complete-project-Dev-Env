package org.meristem.oneapp.coreservices.wealth.domains.requests;

public record TBillSellRequest(String holdingId,
                               Double amount,
                               String customerId) {
}
