package org.meristem.oneapp.walletservice.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;
import org.meristem.oneapp.kafka.dtos.TransactionEventDto;
import org.meristem.oneapp.walletservice.constants.KafkaTopics;
import org.meristem.oneapp.walletservice.domains.enums.*;
import org.meristem.oneapp.walletservice.domains.requests.ProvidusAccountFundedEventRequest;
import org.meristem.oneapp.walletservice.domains.responses.ProvidusTransactionResponse;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.walletservice.mappers.TransactionsMapper;
import org.meristem.oneapp.walletservice.models.*;
import org.meristem.oneapp.walletservice.repositories.*;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionService {


    private final VirtualAccountRepository virtualAccountRepository;
    private final WalletRepository walletRepository;
    private final TransactionsMapper transactionsMapper = TransactionsMapper.INSTANCE;
    private final TransactionsRepository transactionsRepository;
    private final BanksRepository banksRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ProvidusTransactionResponse handleProvidusTransaction(ProvidusAccountFundedEventRequest request) {

        log.info("Processing transaction {}", request.data().sessionId());

        Transactions transactionsExists = transactionsRepository.findByProviderReference(request.data().sessionId());
        if (transactionsExists != null && TransactionStatus.COMPLETED.getValue().equals(transactionsExists.getStatus())) {
            return ProvidusTransactionResponse.builder().message("Transaction already processed").success(false).build();
        }

        VirtualAccounts virtualAccounts = getVirtualAccounts(request.data().accountNumber(), AccountProvider.PROVIDUS.getBankCode())
                .orElseThrow(() -> new BadRequestException("Unable to process transaction"));

        Wallets wallets = getWalletsById(virtualAccounts.getWalletId()).orElseThrow(() -> new BadRequestException("Unable to process transaction"));
        String bankCode = request.data().sessionId().substring(0, 6);
        Banks bankCodes = banksRepository.findBanksByProviderCodeAndBankCode(ProviderCode.PROVIDUS.getValue(), bankCode).orElseThrow(() -> new BadRequestException("Bank does not exist"));

        Transactions transactions = transactionsMapper.providusTransactionsToTransactions(request);
        transactions.setWalletId(wallets.getId());
        transactions.setVirtualAccountId(virtualAccounts.getId());
        BigDecimal currentBalance = wallets.getBalance();
        BigDecimal newBalance = wallets.getBalance().add(request.data().amount());
        transactions.setPreviousBalance(currentBalance);
        transactions.setProviderReference(request.data().reference());

        if ("successful".equalsIgnoreCase(request.data().status())) {
            virtualAccounts.setBalance(virtualAccounts.getBalance().add(request.data().amount()));
            wallets.setBalance(newBalance);
            transactions.setStatus(TransactionStatus.COMPLETED.getValue());
        } else if ("failed".equalsIgnoreCase(request.data().status())) {
            transactions.setStatus(TransactionStatus.FAILED.getValue());
        } else if ("pending".equalsIgnoreCase(request.data().status())) {
            transactions.setStatus(TransactionStatus.PENDING.getValue());
        }
        transactions.setSendersBankCode(bankCode);
        transactions.setSendersBankName(bankCodes.getBankName());
        transactions.setNewBalance(wallets.getBalance());
        transactions.setType(TransactionType.DEPOSIT.getValue());
        transactions.setAmount(request.data().amount());
        transactions.setMethod(TransactionMethod.BANK_TRANSFER.getValue());
        transactions.setTransactionDate(AppUtil.nonNullOrLocalDateTimeNow(request.data().paidAt()));
        transactions.setReference(AppUtil.generateTransactionReference(wallets.getId() + virtualAccounts.getId()));
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("channelCode", request.data().channelCode());
        metadata.put("destinationInstitutionCode", request.data().destinationInstitutionCode());
        metadata.put("BankVerificationCode", request.data().destinationInstitutionCode());
        walletRepository.save(wallets);
        virtualAccountRepository.save(virtualAccounts);

        transactionsRepository.save(transactions);
        TransactionEventDto payload = transactionsMapper.transactionsToTransactionEventDto(transactions);
        payload = payload.withers(payload, virtualAccounts.getAccountNumber(), virtualAccounts.getAccountName(), TransactionType.DEPOSIT.getName(), TransactionMethod.BANK_TRANSFER.getName(), metadata);

        try {
            OutboxEvent event = OutboxEvent.builder()
                    .aggregateId(transactions.getId()).aggregateType(AggregateType.WALLET.getValue())
                    .eventType(KafkaTopics.KAFKA_TRANSACTIONS_TOPIC)
                    .eventClass(TransactionEventDto.class.getName())
                    .outboxStatus(OutboxStatus.PENDING.getValue())
                    .eventKey(transactions.getReference())
                    .payload(objectMapper.writeValueAsString(payload)).build();

            PushNotificationDto pushNotificationDto = PushNotificationDto.builder().body(PushNotifications.TRANSACTION_NOTIFICATION.getBody()).title(PushNotifications.TRANSACTION_NOTIFICATION.getTitle())
                    .userId(wallets.getUserId()).build();
            OutboxEvent event1 = OutboxEvent.builder()
                    .aggregateId(transactions.getId()).aggregateType(AggregateType.WALLET.getValue())
                    .eventType(KafkaTopics.KAFKA_PUSH_NOTIFICATION_TOPIC)
                    .outboxStatus(OutboxStatus.PENDING.getValue())
                    .eventClass(PushNotificationDto.class.getName())
                    .eventKey(transactions.getReference())
                    .payload(objectMapper.writeValueAsString(pushNotificationDto)).build();


            outboxEventRepository.saveAll(List.of(event, event1));
            log.info("Successfully processed transaction and pushed it with id {} to outbox", transactions.getId());
        } catch (JsonProcessingException e) {
            log.error("Error processing transaction with id {} to outbox", transactions.getId(), e);
        }

        return ProvidusTransactionResponse.builder().message("Successful").success(true).build();
    }

    private Optional<VirtualAccounts> getVirtualAccounts(String accountNumber, String bankCode) {
        return virtualAccountRepository.findByAccountNumberAndBankCode(accountNumber, bankCode);
    }

    private Optional<Wallets> getWalletsById(Long walletId) {
        return walletRepository.findWalletsById(walletId);
    }
}
