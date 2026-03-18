package org.meristem.oneapp.wealthservice.domains.requests;

public record TBillPreviewRequest(String instrumentId,
                                  Double amount,
                                  Integer tenorDays,
                                  String customerId) {
}
