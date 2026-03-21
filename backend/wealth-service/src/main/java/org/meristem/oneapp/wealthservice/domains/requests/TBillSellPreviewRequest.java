package org.meristem.oneapp.wealthservice.domains.requests;

public record TBillSellPreviewRequest(String holdingId,
                                      Double amount,
                                      String customerId) {
}
