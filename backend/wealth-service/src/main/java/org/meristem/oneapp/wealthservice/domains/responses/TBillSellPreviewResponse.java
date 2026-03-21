package org.meristem.oneapp.wealthservice.domains.responses;

public record TBillSellPreviewResponse(String holdingId,
                                       Double amount,
                                       Double fee,
                                       Double netAmount) {
}
