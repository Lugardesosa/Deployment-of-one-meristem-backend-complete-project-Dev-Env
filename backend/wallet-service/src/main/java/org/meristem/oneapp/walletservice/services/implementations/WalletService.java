package org.meristem.oneapp.walletservice.services.implementations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.walletservice.domains.responses.WalletBalanceResponse;
import org.meristem.oneapp.walletservice.models.Wallets;
import org.meristem.oneapp.walletservice.repositories.WalletRepository;
import org.meristem.oneapp.walletservice.services.IWalletService;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService implements IWalletService {

    private final WalletRepository walletRepository;

    public Wallets createWallet(KycCompletedDto kycCompletedDto) {
        final Wallets[] savedWallet = new Wallets[1];
        walletRepository.findByUserId(kycCompletedDto.userId()).ifPresentOrElse(w -> savedWallet[0] = w,
                () -> {
                    Wallets wallets = Wallets.builder().userId(kycCompletedDto.userId()).balance(BigDecimal.ZERO).fullName(AppUtil.getUserFullName(kycCompletedDto.firstName(), "", kycCompletedDto.lastName()))
                            .build();
                    savedWallet[0] = walletRepository.save(wallets);
                    log.info("Wallet created for user {}", savedWallet[0].getUserId());
                });
        return savedWallet[0];
    }

    public WalletBalanceResponse getAccountBalance() {

        return new WalletBalanceResponse(walletRepository.findBalanceByUserId(AppUtil.getLoggedInUserId()));
    }
}
