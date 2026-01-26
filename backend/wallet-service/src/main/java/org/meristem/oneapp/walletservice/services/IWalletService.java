package org.meristem.oneapp.walletservice.services;

import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.walletservice.domains.responses.WalletBalanceResponse;
import org.meristem.oneapp.walletservice.models.Wallets;

public interface IWalletService {
    Wallets createWallet(KycCompletedDto kycCompletedDto);
    WalletBalanceResponse getAccountBalance();
}
