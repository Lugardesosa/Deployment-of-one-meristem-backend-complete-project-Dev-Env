package org.meristem.oneapp.wealthservice.domains.requests;

public record TBillSellRequest(String holdingId,
                               Double amount,
                               String customerId) {
}
