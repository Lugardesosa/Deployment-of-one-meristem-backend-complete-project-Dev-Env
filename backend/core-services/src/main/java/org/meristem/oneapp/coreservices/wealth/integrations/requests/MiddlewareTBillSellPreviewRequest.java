package org.meristem.oneapp.coreservices.wealth.integrations.requests;

public record MiddlewareTBillSellPreviewRequest(String holdingId,
                                      Double amount,
                                      String customerId) {
}
