package org.meristem.oneapp.wealthservice.domains.responses;

public record FundAccountsResponse(String accountId,
                                   String customerId,
                                   String fundId,
                                   Double units) {
}
