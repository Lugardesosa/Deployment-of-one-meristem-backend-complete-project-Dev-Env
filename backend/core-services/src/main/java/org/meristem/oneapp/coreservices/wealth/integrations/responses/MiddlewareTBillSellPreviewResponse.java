package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewareTBillSellPreviewResponse(String holdingId,
                                       Double amount,
                                       Double fee,
                                       Double netAmount) {
}
