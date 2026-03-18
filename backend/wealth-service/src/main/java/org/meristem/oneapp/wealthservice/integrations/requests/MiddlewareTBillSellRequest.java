package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewareTBillSellRequest(String holdingId,
                               Double amount,
                               String customerId) {
}
