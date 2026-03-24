package org.meristem.oneapp.coreservices.wealth.domains.requests;

public record TBillSellPreviewRequest(String holdingId,
                                      Double amount,
                                      String customerId) {
}
