package org.meristem.oneapp.wealthservice.domains.responses;

public record TBillPurchaseResponse(String orderId,
                                    String instrumentId,
                                    Double amount,
                                    String status) {
}
