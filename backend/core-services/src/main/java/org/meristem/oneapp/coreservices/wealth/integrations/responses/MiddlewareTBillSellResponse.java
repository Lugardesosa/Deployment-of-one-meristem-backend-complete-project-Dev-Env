package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewareTBillSellResponse(String orderId,
                                String holdingId,
                                Double amount,
                                String status) {
}
