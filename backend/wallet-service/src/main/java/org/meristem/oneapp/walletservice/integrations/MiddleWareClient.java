package org.meristem.oneapp.walletservice.integrations;


import org.meristem.oneapp.walletservice.integrations.requests.MiddlewareWithdrawalRequest;
import org.meristem.oneapp.walletservice.integrations.requests.NameInquiryRequest;
import org.meristem.oneapp.walletservice.integrations.requests.WalletCreateRequest;
import org.meristem.oneapp.walletservice.integrations.requests.WalletTransferRequest;
import org.meristem.oneapp.walletservice.integrations.responses.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE, url = "/wallet")
public interface MiddleWareClient {

    @GetExchange("")
    MiddlewareAppResponse<List<MiddlewareWalletAccountResponse>> getWallets(@RequestParam("customerId") String customerId);

    @GetExchange("/transactions/recent/{accountNo}")
    WalletApiResponse<List<WalletTransactionResponse>> getRecentTransactions(@PathVariable String accountNo);

    @GetExchange("/transactions/{accountNo}")
    WalletApiResponse<List<WalletTransactionResponse>> getTransactions(@PathVariable String accountNo, @RequestParam String startDate, @RequestParam String endDate);

    @PostExchange("/transfer")
    WalletApiResponse<WalletTransferResponse> transferFunds(@RequestBody WalletTransferRequest request);

    @PostExchange("/new")
    WalletApiResponse<MiddlewareWalletAccountResponse> createWallet(@RequestBody WalletCreateRequest request);

    @PostExchange("/payout")
    WalletApiResponse<MiddlewareWithdrawalResponse> withdraw(@RequestBody MiddlewareWithdrawalRequest request);

    @PostExchange("/payout/{transactionId}/status")
    WalletApiResponse<MiddlewareWithdrawalStatusResponse> withdrawalStatus(@PathVariable String transactionId );

    @PostExchange("/payout/banks")
    WalletApiResponse<List<MiddlewareBanksResponse>> banks();

    @PostExchange("/payout/name-inquiry")
    WalletApiResponse<NameInquiryResponse> nameInquiry(@RequestBody NameInquiryRequest request);
}
