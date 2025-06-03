package org.meristem.oneapp.walletservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.walletservice.domains.enums.AccountProvider;
import org.meristem.oneapp.walletservice.domains.enums.TransactionMethod;
import org.meristem.oneapp.walletservice.domains.enums.TransactionType;
import org.meristem.oneapp.walletservice.domains.requests.WemaAccountQueryRequest;
import org.meristem.oneapp.walletservice.domains.requests.WemaTransactionNotificationRequest;
import org.meristem.oneapp.walletservice.domains.responses.WemaAccountQueryResponse;
import org.meristem.oneapp.walletservice.domains.responses.WemaTransactionResponse;
import org.meristem.oneapp.walletservice.mappers.TransactionsMapper;
import org.meristem.oneapp.walletservice.models.Transactions;
import org.meristem.oneapp.walletservice.models.VirtualAccounts;
import org.meristem.oneapp.walletservice.models.Wallets;
import org.meristem.oneapp.walletservice.repositories.GeneralRepository;
import org.meristem.oneapp.walletservice.repositories.TransactionsRepository;
import org.meristem.oneapp.walletservice.repositories.VirtualAccountRepository;
import org.meristem.oneapp.walletservice.repositories.WalletRepository;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class VirtualAccountService {

    private final VirtualAccountRepository virtualAccountRepository;
    private final TransactionsRepository transactionsRepository;
    private final WalletRepository walletRepository;
    private final GeneralRepository generalRepository;
    private final TransactionsMapper transactionsMapper = TransactionsMapper.INSTANCE;

    public void createVirtualAccounts(Long userId) {

    }

    public WemaAccountQueryResponse queryWemaAccount(@Valid WemaAccountQueryRequest request) {

        String fullName = virtualAccountRepository.getFullNameByAccountNumberAndBankCode(request.accountNumber(), AccountProvider.WEMA.getBankCode());
        return WemaAccountQueryResponse.builder().accountName(fullName).status(nonNull(fullName) ? "00" : "07")
                .statusDesc(nonNull(fullName) ? "Account found" : "Account not found").build();
    }


    @Transactional
    public WemaTransactionResponse handleTransaction(WemaTransactionNotificationRequest request) {

        VirtualAccounts virtualAccounts = virtualAccountRepository.findByAccountNumberAndBankCode(request.creditAccount(), AccountProvider.WEMA.getBankCode());

        Wallets wallets = walletRepository.findWalletsById(virtualAccounts.getWalletId());

        Transactions transactions = transactionsMapper.wemaTransactionsToTransactions(request);
        transactions.setWalletId(wallets.getId());
        transactions.setVirtualAccountId(virtualAccounts.getId());
        transactions.setPreviousBalance(wallets.getBalance());

        virtualAccounts.setBalance(virtualAccounts.getBalance().add(request.amount()));
        wallets.setBalance(wallets.getBalance().add(request.amount()));

        transactions.setNewBalance(wallets.getBalance());
        transactions.setType(TransactionType.DEPOSIT.getValue());
        transactions.setMethod(TransactionMethod.BANK_TRANSFER.getValue());
        transactions.setReference(AppUtil.generateTransactionReference(wallets.getId() + virtualAccounts.getId()));
        walletRepository.save(wallets);
        virtualAccountRepository.save(virtualAccounts);

        transactionsRepository.save(transactions);

        return WemaTransactionResponse.builder().transactionReference(transactions.getReference()).status("00")
                .statusDesc("Successful").build();

    }

    public void createWemaAccount(KycCompletedDto value, Long walletId) {

        AccountProvider WEMA = AccountProvider.WEMA;
        String accountNumber = generalRepository.generateWemaVirtualAccount();
        VirtualAccounts virtualAccounts = VirtualAccounts.builder()
                .accountName(value.fullName())
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .bankCode(WEMA.getBankCode())
                .bankName(WEMA.getValue())
                .reference(AppUtil.generateVirtualAccountReference(WEMA, value.userId()))
                .provider(WEMA.getValue())
                .walletId(walletId)
                .build();
        virtualAccountRepository.save(virtualAccounts);
    }
}
