package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record TBillProductResponse(String instrumentId,
                                   Integer tenorDays,
                                   String currency) {
}
