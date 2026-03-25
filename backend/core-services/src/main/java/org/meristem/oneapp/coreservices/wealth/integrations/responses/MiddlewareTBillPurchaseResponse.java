package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewareTBillPurchaseResponse(String orderId,
                                    String instrumentId,
                                    Double amount,
                                    String status) {
}
