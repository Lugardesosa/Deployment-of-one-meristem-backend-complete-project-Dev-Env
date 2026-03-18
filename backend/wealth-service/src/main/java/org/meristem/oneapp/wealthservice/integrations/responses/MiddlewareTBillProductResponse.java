package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewareTBillProductResponse(String instrumentId,
                                   Integer tenorDays,
                                   String currency) {
}
