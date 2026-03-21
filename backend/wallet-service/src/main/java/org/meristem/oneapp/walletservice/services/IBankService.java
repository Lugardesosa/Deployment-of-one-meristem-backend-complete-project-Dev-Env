package org.meristem.oneapp.walletservice.services;

import org.meristem.oneapp.walletservice.domains.enums.ProviderCode;
import org.meristem.oneapp.walletservice.domains.requests.BankAccountRequest;
import org.meristem.oneapp.walletservice.domains.responses.BankAccountResponse;
import org.meristem.oneapp.walletservice.domains.responses.BankCodeResponse;

import java.util.List;

public interface IBankService {
    BankAccountResponse resolveAccount(BankAccountRequest request);
    List<BankCodeResponse> getBanks(ProviderCode providerCode);
}
