package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record TBillSellPreviewResponse(String holdingId,
                                       Double amount,
                                       Double fee,
                                       Double netAmount) {
}
