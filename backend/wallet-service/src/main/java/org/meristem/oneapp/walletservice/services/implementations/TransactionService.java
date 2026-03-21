package org.meristem.oneapp.walletservice.services.implementations;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;
import org.meristem.oneapp.kafka.dtos.TransactionEventDto;
import org.meristem.oneapp.walletservice.constants.KafkaTopics;
import org.meristem.oneapp.walletservice.domains.enums.*;
import org.meristem.oneapp.walletservice.domains.requests.ProvidusAccountFundedEventRequest;
import org.meristem.oneapp.walletservice.domains.requests.WithdrawFundRequest;
import org.meristem.oneapp.walletservice.domains.responses.ProvidusTransactionResponse;
import org.meristem.oneapp.walletservice.domains.responses.WithdrawFundResponse;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.walletservice.mappers.TransactionsMapper;
import org.meristem.oneapp.walletservice.models.*;
import org.meristem.oneapp.walletservice.repositories.*;
import org.meristem.oneapp.walletservice.services.ITransactionService;
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
public class TransactionService implements ITransactionService {


    private final VirtualAccountRepository virtualAccountRepository;
    private final WalletRepository walletRepository;
    private final TransactionsMapper transactionsMapper = TransactionsMapper.INSTANCE;
    private final TransactionsRepository transactionsRepository;
    private final BanksRepository banksRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ProvidusTransactionResponse handleProvidusTransaction(ProvidusAccountFundedEventRequest request) {

        return ProvidusTransactionResponse.builder().message("Successful").success(true).build();
    }

    @Override
    public WithdrawFundResponse withdrawFund(WithdrawFundRequest request) {
        return null;
    }
}
