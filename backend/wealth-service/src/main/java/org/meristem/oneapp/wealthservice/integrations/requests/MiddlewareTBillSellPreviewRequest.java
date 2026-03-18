package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewareTBillSellPreviewRequest(String holdingId,
                                      Double amount,
                                      String customerId) {
}
