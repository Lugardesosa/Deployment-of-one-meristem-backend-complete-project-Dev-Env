package org.meristem.oneapp.coreservices.wealth.integrations.requests;

public record MiddlewareTBillSellRequest(String holdingId,
                               Double amount,
                               String customerId) {
}
