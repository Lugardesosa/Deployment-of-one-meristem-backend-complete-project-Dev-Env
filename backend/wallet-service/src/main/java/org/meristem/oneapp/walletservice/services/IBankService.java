package org.meristem.oneapp.walletservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.walletservice.domains.enums.ProviderCode;
import org.meristem.oneapp.walletservice.domains.requests.AddAccountRequest;
import org.meristem.oneapp.walletservice.domains.requests.BankAccountRequest;
import org.meristem.oneapp.walletservice.domains.requests.BankDetailsQueryRequest;
import org.meristem.oneapp.walletservice.domains.responses.BankAccountResponse;
import org.meristem.oneapp.walletservice.domains.responses.BankCodeResponse;
import org.meristem.oneapp.walletservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.walletservice.integrations.responses.UpdateResponse;

import java.util.List;

public interface IBankService {
    BankAccountResponse resolveAccount(BankAccountRequest request);
    List<BankCodeResponse> getBanks(ProviderCode providerCode);
    BankAccountResponse bankDetailsQuery(BankDetailsQueryRequest request);

    UpdateResponse addAccount(@Valid AddAccountRequest request);
}
