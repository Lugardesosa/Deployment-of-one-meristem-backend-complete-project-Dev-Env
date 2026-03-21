package org.meristem.oneapp.walletservice.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.domains.responses.TransactionResponse;
import org.meristem.oneapp.walletservice.integrations.MiddleWareClient;
import org.meristem.oneapp.walletservice.integrations.responses.WalletTransactionResponse;
import org.meristem.oneapp.walletservice.mappers.TransactionsMapper;
import org.meristem.oneapp.walletservice.services.IWalletTransactionService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletTransactionService implements IWalletTransactionService {

    private final MiddleWareClient middleWareClient;
    private final TransactionsMapper transactionsMapper = TransactionsMapper.INSTANCE;

    @Override
    public List<TransactionResponse> getRecentTransactions(String accountNo) {
        return transactionsMapper.walletTransactionResponseToTransactionResponse(normalizeList(middleWareClient.getRecentTransactions(accountNo).data()));
    }

    @Override
    public List<TransactionResponse> getTransactions(String accountNo) {
        return transactionsMapper.walletTransactionResponseToTransactionResponse(normalizeList(middleWareClient.getTransactions(accountNo).data()));
    }

    private <T> List<T> normalizeList(List<T> response) {
        return response == null ? Collections.emptyList() : response;
    }
}
