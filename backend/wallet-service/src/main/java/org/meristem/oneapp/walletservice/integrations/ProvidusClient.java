package org.meristem.oneapp.walletservice.integrations;


import org.meristem.oneapp.walletservice.integrations.requests.CreateProvidusWalletRequest;
import org.meristem.oneapp.walletservice.integrations.responses.CreateProvidusWalletResponse;
import org.meristem.oneapp.walletservice.integrations.responses.ProvidusSingleWalletResponse;
import org.meristem.oneapp.walletservice.integrations.responses.ProvidusWalletsResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange()
public interface ProvidusClient {

    @PostExchange(url = "wallet")
    CreateProvidusWalletResponse createWallet(@RequestBody CreateProvidusWalletRequest request);

    @GetExchange(url = "wallet")
    ProvidusWalletsResponse getWallets();

    @GetExchange(url = "wallet/customer")
    ProvidusSingleWalletResponse getWallet(@RequestParam String customerId);
}
