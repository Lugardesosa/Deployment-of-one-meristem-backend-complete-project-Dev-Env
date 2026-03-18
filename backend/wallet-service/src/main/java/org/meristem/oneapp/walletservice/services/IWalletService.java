package org.meristem.oneapp.walletservice.services;

import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.walletservice.domains.responses.WalletBalanceResponse;

public interface IWalletService {
    void createWallet(KycCompletedDto kycCompletedDto);
    WalletBalanceResponse getAccountBalance();
}
