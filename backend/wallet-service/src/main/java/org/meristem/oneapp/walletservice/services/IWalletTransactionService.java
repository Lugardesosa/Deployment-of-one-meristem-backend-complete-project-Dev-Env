package org.meristem.oneapp.walletservice.services;

import org.meristem.oneapp.walletservice.domains.responses.TransactionResponse;

import java.util.List;

public interface IWalletTransactionService {
    List<TransactionResponse> getRecentTransactions(String accountNo);
    List<TransactionResponse> getTransactions(String accountNo);
}
