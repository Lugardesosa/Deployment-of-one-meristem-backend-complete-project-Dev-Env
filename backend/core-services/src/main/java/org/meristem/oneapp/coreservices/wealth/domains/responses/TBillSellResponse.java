package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record TBillSellResponse(String orderId,
                                String holdingId,
                                Double amount,
                                String status) {
}
