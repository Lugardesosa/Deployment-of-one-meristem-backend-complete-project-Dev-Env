package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewareTBillPreviewRequest(String instrumentId,
                                  Double amount,
                                  Integer tenorDays,
                                  String customerId) {
}
