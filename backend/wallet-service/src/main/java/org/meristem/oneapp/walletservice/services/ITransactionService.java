package org.meristem.oneapp.walletservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.walletservice.domains.requests.ProvidusAccountFundedEventRequest;
import org.meristem.oneapp.walletservice.domains.requests.WithdrawFundRequest;
import org.meristem.oneapp.walletservice.domains.responses.ProvidusTransactionResponse;
import org.meristem.oneapp.walletservice.domains.responses.WithdrawFundResponse;

public interface ITransactionService {
    ProvidusTransactionResponse handleProvidusTransaction(ProvidusAccountFundedEventRequest request);

    WithdrawFundResponse withdrawFund(@Valid WithdrawFundRequest request);
}
