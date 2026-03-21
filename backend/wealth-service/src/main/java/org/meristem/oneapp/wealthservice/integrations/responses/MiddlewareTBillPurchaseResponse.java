package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewareTBillPurchaseResponse(String orderId,
                                    String instrumentId,
                                    Double amount,
                                    String status) {
}
