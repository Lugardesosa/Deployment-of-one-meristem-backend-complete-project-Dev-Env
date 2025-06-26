package org.meristem.oneapp.walletservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.meristem.oneapp.walletservice.domains.enums.AccountProvider;
import org.meristem.oneapp.walletservice.domains.enums.TransactionMethod;
import org.meristem.oneapp.walletservice.domains.enums.TransactionStatus;
import org.meristem.oneapp.walletservice.domains.enums.TransactionType;
import org.meristem.oneapp.walletservice.domains.requests.ProvidusAccountFundedEventRequest;
import org.meristem.oneapp.walletservice.domains.requests.WemaTransactionNotificationRequest;
import org.meristem.oneapp.walletservice.domains.responses.ProvidusTransactionResponse;
import org.meristem.oneapp.walletservice.domains.responses.WemaTransactionResponse;
import org.meristem.oneapp.walletservice.mappers.TransactionsMapper;
import org.meristem.oneapp.walletservice.models.ProvidusBankCodes;
import org.meristem.oneapp.walletservice.models.Transactions;
import org.meristem.oneapp.walletservice.models.VirtualAccounts;
import org.meristem.oneapp.walletservice.models.Wallets;
import org.meristem.oneapp.walletservice.repositories.ProvidusBankCodesRepository;
import org.meristem.oneapp.walletservice.repositories.TransactionsRepository;
import org.meristem.oneapp.walletservice.repositories.VirtualAccountRepository;
import org.meristem.oneapp.walletservice.repositories.WalletRepository;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionService {


    private final VirtualAccountRepository virtualAccountRepository;
    private final WalletRepository walletRepository;
    private final TransactionsMapper transactionsMapper = Mappers.getMapper(TransactionsMapper.class);
    private final TransactionsRepository transactionsRepository;
    private final ProvidusBankCodesRepository providusBankCodesRepository;

    @Transactional
    public WemaTransactionResponse handleWemaTransaction(WemaTransactionNotificationRequest request) {

        AtomicReference<String> wemaReference = new AtomicReference<>("");
        getVirtualAccounts(request.creditAccount(),  AccountProvider.WEMA.getBankCode()).ifPresent(virtualAccounts -> {

            getWalletsById(virtualAccounts.getWalletId()).ifPresent(wallets -> {

                Transactions transactions = transactionsMapper.wemaTransactionsToTransactions(request);
                transactions.setWalletId(wallets.getId());
                transactions.setVirtualAccountId(virtualAccounts.getId());
                transactions.setPreviousBalance(wallets.getBalance());

                virtualAccounts.setBalance(virtualAccounts.getBalance().add(request.amount()));
                wallets.setBalance(wallets.getBalance().add(request.amount()));

                transactions.setNewBalance(wallets.getBalance());
                wemaReference.set(transactions.getReference());
                transactions.setType(TransactionType.DEPOSIT.getValue());
                transactions.setMethod(TransactionMethod.BANK_TRANSFER.getValue());
                transactions.setReference(AppUtil.generateTransactionReference(wallets.getId() + virtualAccounts.getId()));
                walletRepository.save(wallets);
                virtualAccountRepository.save(virtualAccounts);
                transactionsRepository.save(transactions);

            });
        });

        return WemaTransactionResponse.builder().transactionReference(wemaReference.get()).status("00")
                .statusDesc("Successful").build();
    }


    @Transactional
    public ProvidusTransactionResponse handleProvidusTransaction(ProvidusAccountFundedEventRequest request) {

        getVirtualAccounts(request.data().accountNumber(),  AccountProvider.PROVIDUS.getBankCode()).ifPresent(virtualAccounts -> {

            getWalletsById(virtualAccounts.getWalletId()).ifPresent(wallets -> {
                String bankCode = request.data().sessionId().substring(0, 6);
                ProvidusBankCodes bankCodes = providusBankCodesRepository.findByBankCode(bankCode);

                Transactions transactions = transactionsMapper.providusTransactionsToTransactions(request);
                transactions.setWalletId(wallets.getId());
                transactions.setVirtualAccountId(virtualAccounts.getId());
                transactions.setPreviousBalance(wallets.getBalance());

                if ("successful".equals(request.data().status())) {
                    virtualAccounts.setBalance(virtualAccounts.getBalance().add(request.data().amount()));
                    wallets.setBalance(wallets.getBalance().add(request.data().amount()));
                    transactions.setStatus(TransactionStatus.COMPLETED.getValue());
                } else if ("failed".equals(request.data().status())) {
                    transactions.setStatus(TransactionStatus.FAILED.getValue());
                } else if ("pending".equals(request.data().status())) {
                    transactions.setStatus(TransactionStatus.PENDING.getValue());
                }
                transactions.setSendersBankCode(bankCode);
                transactions.setSendersBankName(bankCodes.getBankName());
                transactions.setNewBalance(wallets.getBalance());
                transactions.setType(TransactionType.DEPOSIT.getValue());
                transactions.setAmount(request.data().amount());
                transactions.setMethod(TransactionMethod.BANK_TRANSFER.getValue());
                transactions.setReference(AppUtil.generateTransactionReference(wallets.getId() + virtualAccounts.getId()));
                walletRepository.save(wallets);
                virtualAccountRepository.save(virtualAccounts);

                transactionsRepository.save(transactions);
            });
        });

        return ProvidusTransactionResponse.builder().message("Successful").success(true).build();
    }

    private Optional<VirtualAccounts> getVirtualAccounts(String accountNumber, String bankCode) {
        return virtualAccountRepository.findByAccountNumberAndBankCode(accountNumber, bankCode);
    }

    private Optional<Wallets> getWalletsById(Long walletId) {
        return walletRepository.findWalletsById(walletId);
    }
}
