package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record TBillPurchaseResponse(String orderId,
                                    String instrumentId,
                                    Double amount,
                                    String status) {
}
