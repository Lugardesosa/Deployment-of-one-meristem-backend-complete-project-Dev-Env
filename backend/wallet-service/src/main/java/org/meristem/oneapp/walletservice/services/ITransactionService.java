package org.meristem.oneapp.walletservice.services;

import org.meristem.oneapp.walletservice.domains.requests.ProvidusAccountFundedEventRequest;
import org.meristem.oneapp.walletservice.domains.responses.ProvidusTransactionResponse;

public interface ITransactionService {
    ProvidusTransactionResponse handleProvidusTransaction(ProvidusAccountFundedEventRequest request);
}
