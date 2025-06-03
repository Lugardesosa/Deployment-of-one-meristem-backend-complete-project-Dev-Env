package org.meristem.oneapp.walletservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.walletservice.models.Wallets;
import org.meristem.oneapp.walletservice.repositories.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallets createWallet(KycCompletedDto kycCompletedDto) {
        Wallets wallets = Wallets.builder().userId(kycCompletedDto.userId()).balance(BigDecimal.ZERO).fullName(kycCompletedDto.fullName())
                .build();
        return walletRepository.save(wallets);
    }
}
