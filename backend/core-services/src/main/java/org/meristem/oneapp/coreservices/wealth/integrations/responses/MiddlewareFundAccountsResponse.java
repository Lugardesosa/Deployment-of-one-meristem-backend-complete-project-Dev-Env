package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewareFundAccountsResponse(String accountId,
                                   String customerId,
                                   String fundId,
                                   Double units) {
}
