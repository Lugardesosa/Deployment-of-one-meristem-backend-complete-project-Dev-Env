package org.meristem.oneapp.walletservice.services;

import org.meristem.oneapp.kafka.dtos.UserCreatedDto;
import org.meristem.oneapp.walletservice.domains.requests.AddAccountNumberRequest;
import org.meristem.oneapp.walletservice.domains.responses.WalletAccountResponse;
import org.meristem.oneapp.walletservice.integrations.requests.WithdrawalRequest;
import org.meristem.oneapp.walletservice.integrations.responses.UpdateResponse;

import java.util.List;

public interface IWalletAccountService {
    List<WalletAccountResponse> getAccounts();

    void create(UserCreatedDto userCreatedDto);

    UpdateResponse addAccountNo(AddAccountNumberRequest userCreatedDto);

    UpdateResponse withdraw(WithdrawalRequest request);
}
