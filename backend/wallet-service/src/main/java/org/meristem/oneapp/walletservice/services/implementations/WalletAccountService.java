package org.meristem.oneapp.walletservice.services.implementations;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.kafka.dtos.UserCreatedDto;
import org.meristem.oneapp.walletservice.domains.responses.WalletAccountResponse;
import org.meristem.oneapp.walletservice.integrations.MiddleWareClient;
import org.meristem.oneapp.walletservice.integrations.requests.WalletCreateRequest;
import org.meristem.oneapp.walletservice.mappers.WalletMapper;
import org.meristem.oneapp.walletservice.models.Wallets;
import org.meristem.oneapp.walletservice.repositories.WalletRepository;
import org.meristem.oneapp.walletservice.services.IWalletAccountService;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletAccountService implements IWalletAccountService {

    private final MiddleWareClient middleWareClient;
    private final WalletMapper walletMapper = WalletMapper.INSTANCE;
    private final HttpServletRequest httpServletRequest;
    private final WalletRepository walletRepository;

    @Override
    public List<WalletAccountResponse> getAccounts() {
        return walletMapper.
                walletVirtualAccountResponseToVirtualAccountResponse(middleWareClient.getWallets(AppUtil.getCustomerId(httpServletRequest)).data());
    }

    @Override
    public void create(UserCreatedDto userCreatedDto) {
        WalletCreateRequest request = WalletCreateRequest.builder().customerId(userCreatedDto.middleWareCustomerId()).currency("NGN").build();
        WalletAccountResponse response = walletMapper.
                walletVirtualAccountResponseToVirtualAccountResponse(middleWareClient.createWallet(request).data());

        Wallets wallets = Wallets.builder()
                .walletId(response.walletId())
                .symplusAccountNo(response.symplusAccountNo())
                .customerId(request.customerId())
                .build();
        walletRepository.save(wallets);
    }
}
