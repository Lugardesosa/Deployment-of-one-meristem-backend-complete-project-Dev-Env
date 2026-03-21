package org.meristem.oneapp.walletservice.services.implementations;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.integrations.MiddleWareClient;
import org.meristem.oneapp.walletservice.integrations.requests.WalletCreateRequest;
import org.meristem.oneapp.walletservice.integrations.requests.WalletTransferRequest;
import org.meristem.oneapp.walletservice.integrations.responses.WalletCreateResponse;
import org.meristem.oneapp.walletservice.integrations.responses.WalletTransferResponse;
import org.meristem.oneapp.walletservice.services.IWalletOperationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletOperationService implements IWalletOperationService {

    private final MiddleWareClient middleWareClient;

    @Override
    public WalletTransferResponse transferFunds(WalletTransferRequest request) {
        return middleWareClient.transferFunds(request).data();
    }

}
