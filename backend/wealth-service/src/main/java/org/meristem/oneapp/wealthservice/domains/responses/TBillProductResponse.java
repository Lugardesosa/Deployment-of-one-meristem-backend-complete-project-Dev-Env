package org.meristem.oneapp.wealthservice.domains.responses;

public record TBillProductResponse(String instrumentId,
                                   Integer tenorDays,
                                   String currency) {
}
