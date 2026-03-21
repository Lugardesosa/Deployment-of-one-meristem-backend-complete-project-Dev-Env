package org.meristem.oneapp.walletservice.services;

import org.meristem.oneapp.walletservice.integrations.requests.WalletCreateRequest;
import org.meristem.oneapp.walletservice.integrations.requests.WalletTransferRequest;
import org.meristem.oneapp.walletservice.integrations.responses.WalletCreateResponse;
import org.meristem.oneapp.walletservice.integrations.responses.WalletTransferResponse;

public interface IWalletOperationService {
    WalletTransferResponse transferFunds(WalletTransferRequest request);
}
