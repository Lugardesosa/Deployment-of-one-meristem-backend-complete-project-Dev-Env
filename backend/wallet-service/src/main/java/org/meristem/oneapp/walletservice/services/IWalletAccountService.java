package org.meristem.oneapp.walletservice.services;

import org.meristem.oneapp.kafka.dtos.UserCreatedDto;
import org.meristem.oneapp.walletservice.domains.responses.WalletAccountResponse;

import java.util.List;

public interface IWalletAccountService {
    List<WalletAccountResponse> getAccounts();

    void create(UserCreatedDto userCreatedDto);
}
