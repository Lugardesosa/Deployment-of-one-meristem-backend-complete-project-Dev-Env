package org.meristem.oneapp.walletservice.services.implementations;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.domains.requests.TransactionRequest;
import org.meristem.oneapp.walletservice.domains.responses.TransactionResponse;
import org.meristem.oneapp.walletservice.integrations.MiddleWareClient;
import org.meristem.oneapp.walletservice.integrations.responses.WalletTransactionResponse;
import org.meristem.oneapp.walletservice.mappers.TransactionsMapper;
import org.meristem.oneapp.walletservice.services.IWalletTransactionService;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletTransactionService implements IWalletTransactionService {

    private final MiddleWareClient middleWareClient;
    private final TransactionsMapper transactionsMapper = TransactionsMapper.INSTANCE;
    private final HttpServletRequest httpServletRequest;

    @Override
    public List<TransactionResponse> getRecentTransactions() {
        String accountNo = AppUtil.getWalletId(httpServletRequest);
        return transactionsMapper.walletTransactionResponseToTransactionResponse(normalizeList(middleWareClient.getRecentTransactions(accountNo).data()));
    }

    @Override
    public List<TransactionResponse> getTransactions(TransactionRequest request) {
        return transactionsMapper.walletTransactionResponseToTransactionResponse(normalizeList(middleWareClient.getTransactions(AppUtil.getWalletId(httpServletRequest), request.startDate().toString(), request.endDate().toString()).data()));
    }

    private <T> List<T> normalizeList(List<T> response) {
        return response == null ? Collections.emptyList() : response;
    }
}
