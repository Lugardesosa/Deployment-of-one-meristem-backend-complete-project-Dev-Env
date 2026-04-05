package org.meristem.oneapp.walletservice.services.implementations;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.UserCreatedDto;
import org.meristem.oneapp.kafka.dtos.WalletCreatedDto;
import org.meristem.oneapp.walletservice.constants.KafkaTopics;
import org.meristem.oneapp.walletservice.domains.enums.AggregateType;
import org.meristem.oneapp.walletservice.domains.enums.OutboxStatus;
import org.meristem.oneapp.walletservice.domains.requests.AddAccountNumberRequest;
import org.meristem.oneapp.walletservice.domains.responses.WalletAccountResponse;
import org.meristem.oneapp.walletservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.walletservice.integrations.MiddleWareClient;
import org.meristem.oneapp.walletservice.integrations.requests.MiddlewareWithdrawalRequest;
import org.meristem.oneapp.walletservice.integrations.requests.WalletCreateRequest;
import org.meristem.oneapp.walletservice.integrations.requests.WithdrawalRequest;
import org.meristem.oneapp.walletservice.integrations.responses.MiddlewareWithdrawalResponse;
import org.meristem.oneapp.walletservice.integrations.responses.UpdateResponse;
import org.meristem.oneapp.walletservice.mappers.WalletMapper;
import org.meristem.oneapp.walletservice.models.OutboxEvent;
import org.meristem.oneapp.walletservice.models.Wallets;
import org.meristem.oneapp.walletservice.repositories.OutboxEventRepository;
import org.meristem.oneapp.walletservice.repositories.WalletRepository;
import org.meristem.oneapp.walletservice.services.IWalletAccountService;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletAccountService implements IWalletAccountService {

    private final MiddleWareClient middleWareClient;
    private final WalletMapper walletMapper = WalletMapper.INSTANCE;
    private final HttpServletRequest httpServletRequest;
    private final WalletRepository walletRepository;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepository;

    @Override
    public List<WalletAccountResponse> getAccounts() {
        return walletMapper.
                walletVirtualAccountResponseToVirtualAccountResponse(middleWareClient.getWallets(AppUtil.getCustomerId(httpServletRequest)).data());
    }

    @Transactional
    @Override
    public void create(UserCreatedDto userCreatedDto) {
        WalletCreateRequest request = WalletCreateRequest.builder().customerId(userCreatedDto.middleWareCustomerId()).currency("NGN").build();
        WalletAccountResponse response = walletMapper.
                walletVirtualAccountResponseToVirtualAccountResponse(middleWareClient.createWallet(request).data());

        Wallets wallets = Wallets.builder()
                .walletId(response.walletId())
                .symplusAccountNo(response.symplusAccountNo())
                .customerId(request.customerId())
                .build();

        wallets = walletRepository.save(wallets);

        WalletCreatedDto walletCreatedDto = WalletCreatedDto.builder().walletId(response.walletId()).build();
        OutboxEvent customer = OutboxEvent.builder()
                .aggregateId(wallets.getId()).aggregateType(AggregateType.WALLET.getValue())
                .eventType(KafkaTopics.KAFKA_WALLET_CREATED_SUCCESS_TOPIC)
                .outboxStatus(OutboxStatus.PENDING.getValue())
                .eventClass(WalletCreatedDto.class.getName())
                .eventKey(request.customerId())
                .payload(objectMapper.writeValueAsString(walletCreatedDto)).build();
        outboxEventRepository.save(customer);
    }


    @Transactional
    @Override
    public UpdateResponse addAccountNo(AddAccountNumberRequest request) {

        String walletId = AppUtil.getWalletId(httpServletRequest);
        Wallets wallets = walletRepository.findWalletsByWalletId(walletId).orElseThrow(() -> new ResourceNotFoundException("Wallet not found", "Wallets", walletId));
        // TODO: Validate account number
        wallets.setWithdrawalBankAccountNo(request.accountNumber());
        wallets.setWithdrawalBankAccountCode(request.bankCode());
        // TODO: set name gotten from validation
        wallets.setWithdrawalBankAccountName("");
        // TODO: update middleware
        return UpdateResponse.builder().message("Successful").success(true).build();
    }

    @Override
    public UpdateResponse withdraw(WithdrawalRequest request) {
        String walletId = AppUtil.getWalletId(httpServletRequest);
        Wallets wallets = walletRepository.findWalletsByWalletId(walletId).orElseThrow(() -> new ResourceNotFoundException("Wallet not found", "Wallets", walletId));
        MiddlewareWithdrawalRequest middlewareWithdrawalRequest = MiddlewareWithdrawalRequest.builder()
                .amount(request.amount())
                .walletId(walletId)
                .beneficiaryAccountName(wallets.getWithdrawalBankAccountName())
                .beneficiaryAccountNumber(wallets.getWithdrawalBankAccountNo())
                .beneficiaryBankCode(wallets.getWithdrawalBankAccountCode())
                .narration(request.narration())
                .requestId(AppUtil.generateReference(20))
                .build();
        MiddlewareWithdrawalResponse response = middleWareClient.withdraw(middlewareWithdrawalRequest).data();
        if (response.status().compareToIgnoreCase("success") == 0) {
            return UpdateResponse.builder().message("Successful").success(true).build();
        }
        return UpdateResponse.builder().message(response.message()).success(false).build();
    }
}
