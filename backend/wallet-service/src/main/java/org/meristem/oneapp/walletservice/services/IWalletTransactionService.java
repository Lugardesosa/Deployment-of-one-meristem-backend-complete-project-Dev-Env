package org.meristem.oneapp.walletservice.services;

import org.meristem.oneapp.walletservice.domains.requests.TransactionRequest;
import org.meristem.oneapp.walletservice.domains.responses.TransactionResponse;

import java.util.List;

public interface IWalletTransactionService {
    List<TransactionResponse> getRecentTransactions();
    List<TransactionResponse> getTransactions(TransactionRequest request);
}
