package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewareFundAccountsResponse(String accountId,
                                   String customerId,
                                   String fundId,
                                   Double units) {
}
