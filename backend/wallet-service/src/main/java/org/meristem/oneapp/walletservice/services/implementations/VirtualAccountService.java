package org.meristem.oneapp.walletservice.services.implementations;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.walletservice.domains.enums.AccountProvider;
import org.meristem.oneapp.walletservice.domains.requests.WemaAccountQueryRequest;
import org.meristem.oneapp.walletservice.domains.responses.VirtualAccountResponse;
import org.meristem.oneapp.walletservice.domains.responses.WemaAccountQueryResponse;
import org.meristem.oneapp.walletservice.integrations.ProvidusClient;
import org.meristem.oneapp.walletservice.integrations.requests.CreateProvidusWalletRequest;
import org.meristem.oneapp.walletservice.integrations.responses.CreateProvidusWalletResponse;
import org.meristem.oneapp.walletservice.models.VirtualAccounts;
import org.meristem.oneapp.walletservice.models.Wallets;
import org.meristem.oneapp.walletservice.repositories.VirtualAccountRepository;
import org.meristem.oneapp.walletservice.services.IVirtualAccountService;
import org.meristem.oneapp.walletservice.services.IWalletService;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class VirtualAccountService implements IVirtualAccountService {

    private final VirtualAccountRepository virtualAccountRepository;
    private final IWalletService walletService;

    private final ProvidusClient providusClient;

    public void createVirtualAccounts(KycCompletedDto record) {
        Wallets wallets = walletService.createWallet(record);
        createProvidusAccount(record, wallets.getId());

    }

    private void createProvidusAccount(KycCompletedDto record, Long walletId) {

        AccountProvider PROVIDUS = AccountProvider.PROVIDUS;

        virtualAccountRepository.findByBankCodeAndWalletId(PROVIDUS.getBankCode(), walletId).ifPresentOrElse(v -> {
                },
                () -> {

                    CreateProvidusWalletRequest request = CreateProvidusWalletRequest.builder().metadata(new HashMap<>())
                            .bvn(record.bvn()).address(record.address()).email(record.email()).dateOfBirth(record.dob())
                            .firstName(record.firstName()).lastName(record.lastName()).phoneNumber(record.phoneNumber()).build();
                    CreateProvidusWalletResponse response = providusClient.createWallet(request);

                    VirtualAccounts virtualAccounts = VirtualAccounts.builder()
                            .accountName(response.wallet().accountName())
                            .accountNumber(response.wallet().accountNumber())
                            .balance(response.wallet().availableBalance())
                            .bankCode(PROVIDUS.getBankCode())
                            .bankName(PROVIDUS.getBankName())
                            .reference(AppUtil.generateVirtualAccountReference(PROVIDUS, record.userId()))
                            .providerWalletId(response.wallet().walletId())
                            .walletId(walletId)
                            .build();
                    virtualAccountRepository.save(virtualAccounts);
                    log.info("Providus Virtual account created for user {} with account number {}", record.userId(), virtualAccounts.getAccountNumber());
                });
    }

    public WemaAccountQueryResponse queryWemaAccount(@Valid WemaAccountQueryRequest request) {

        String fullName = virtualAccountRepository.getFullNameByAccountNumberAndBankCode(request.accountNumber(), AccountProvider.WEMA.getBankCode());
        return WemaAccountQueryResponse.builder().accountName(fullName).status(nonNull(fullName) ? "00" : "07")
                .statusDesc(nonNull(fullName) ? "Account found" : "Account not found").build();
    }


    public List<VirtualAccountResponse> getAccounts() {
        Long userId = AppUtil.getLoggedInUserId();
        return virtualAccountRepository.findAccountNumberAndBankNameByUserId(userId);
    }
}
