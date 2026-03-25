package org.meristem.oneapp.coreservices.wealth.domains.requests;

public record TBillPreviewRequest(String instrumentId,
                                  Double amount,
                                  Integer tenorDays,
                                  String customerId) {
}
