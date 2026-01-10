package org.meristem.oneapp.walletservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.walletservice.domains.responses.WalletBalanceResponse;
import org.meristem.oneapp.walletservice.models.Wallets;
import org.meristem.oneapp.walletservice.repositories.WalletRepository;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallets createWallet(KycCompletedDto kycCompletedDto) {
        Wallets wallets = Wallets.builder().userId(kycCompletedDto.userId()).balance(BigDecimal.ZERO).fullName(AppUtil.getUserFullName(kycCompletedDto.firstName(), "", kycCompletedDto.lastName()))
                .build();
        Wallets savedWallet = walletRepository.save(wallets);
        log.info("Wallet created for user {}", savedWallet.getUserId());
        return savedWallet;
    }

    public WalletBalanceResponse getAccountBalance() {

        return new WalletBalanceResponse(walletRepository.findBalanceByUserId(AppUtil.getLoggedInUserId()));
    }
}
