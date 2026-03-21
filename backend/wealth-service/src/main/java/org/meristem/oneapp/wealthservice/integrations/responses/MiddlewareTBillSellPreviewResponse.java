package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewareTBillSellPreviewResponse(String holdingId,
                                       Double amount,
                                       Double fee,
                                       Double netAmount) {
}
