package org.meristem.oneapp.walletservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.walletservice.domains.enums.ProviderCode;
import org.meristem.oneapp.walletservice.domains.requests.BankAccountRequest;
import org.meristem.oneapp.walletservice.domains.responses.BankAccountResponse;
import org.meristem.oneapp.walletservice.domains.responses.BankCodeResponse;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.walletservice.integrations.PaystackClient;
import org.meristem.oneapp.walletservice.integrations.responses.AccountQueryResult;
import org.meristem.oneapp.walletservice.repositories.BanksRepository;
import org.meristem.oneapp.walletservice.repositories.CustomRepository;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankService {

    private final BanksRepository banksRepository;
    private final PaystackClient paystackClient;

    public BankAccountResponse resolveAccount(BankAccountRequest request) {

        AccountQueryResult result = paystackClient.getBalance(request.bankCode(), request.accountNumber());
        if (result.status()) {
            return new BankAccountResponse(AppUtil.titleCase(result.data().accountName()));
        }
        throw new BadRequestException("Unable to resolve account");
    }

    public List<BankCodeResponse> getBanks(ProviderCode providerCode) {
        return banksRepository.findBanksByProviderCode(providerCode.getValue());
    }
}
