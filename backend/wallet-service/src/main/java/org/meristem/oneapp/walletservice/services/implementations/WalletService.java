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

    public void createWallet(KycCompletedDto kycCompletedDto) {
    }

    public WalletBalanceResponse getAccountBalance() {

        return WalletBalanceResponse.builder()
                .ng(WalletBalanceResponse.CurrencyBalance.builder()
                        .currencyCode("NGG")
                        .balance(AppUtil.generateRandomBigDecimalFromRange(new BigDecimal("1000"), new BigDecimal("10000000000"), 2))
                        .build())
                .us(WalletBalanceResponse.CurrencyBalance.builder()
                        .currencyCode("USD")
                        .balance(AppUtil.generateRandomBigDecimalFromRange(new BigDecimal("100"), new BigDecimal("10000"), 2))
                        .build())
                .uk(WalletBalanceResponse.CurrencyBalance.builder()
                        .currencyCode("GBP")
                        .balance(AppUtil.generateRandomBigDecimalFromRange(new BigDecimal("100"), new BigDecimal("8000"), 2))
                        .build())
                .build();
    }
}
