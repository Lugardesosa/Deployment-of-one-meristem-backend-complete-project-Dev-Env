package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewareTBillProductResponse(String instrumentId,
                                   Integer tenorDays,
                                   String currency) {
}
