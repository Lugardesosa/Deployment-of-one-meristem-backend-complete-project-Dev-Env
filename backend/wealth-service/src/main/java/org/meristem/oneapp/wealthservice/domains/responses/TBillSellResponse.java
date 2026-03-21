package org.meristem.oneapp.wealthservice.domains.responses;

public record TBillSellResponse(String orderId,
                                String holdingId,
                                Double amount,
                                String status) {
}
