package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewareTBillSellResponse(String orderId,
                                String holdingId,
                                Double amount,
                                String status) {
}
