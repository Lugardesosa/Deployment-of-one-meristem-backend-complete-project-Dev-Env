package org.meristem.oneapp.coreservices.wealth.integrations.requests;

public record MiddlewareTBillPreviewRequest(String instrumentId,
                                  Double amount,
                                  Integer tenorDays,
                                  String customerId) {
}
