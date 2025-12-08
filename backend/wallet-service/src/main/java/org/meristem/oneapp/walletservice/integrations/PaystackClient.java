package org.meristem.oneapp.walletservice.integrations;


import org.meristem.oneapp.walletservice.integrations.responses.AccountQueryResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface PaystackClient {

    @GetExchange(url = "/bank/resolve")
    AccountQueryResult getBalance(@RequestParam(name = "bank_code") String bankCode, @RequestParam(name = "account_number") String accountNumber);
}
