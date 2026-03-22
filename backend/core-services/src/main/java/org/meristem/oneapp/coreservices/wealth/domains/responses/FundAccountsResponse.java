package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record FundAccountsResponse(String accountId,
                                   String customerId,
                                   String fundId,
                                   Double units) {
}
