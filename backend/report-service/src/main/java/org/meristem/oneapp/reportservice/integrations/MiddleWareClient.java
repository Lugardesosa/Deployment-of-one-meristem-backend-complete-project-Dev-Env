package org.meristem.oneapp.reportservice.integrations;


import org.meristem.oneapp.reportservice.integrations.responses.MiddleWareTransactionResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface MiddleWareClient {

    @GetExchange("/wallet/transactions/recent/{customerId}")
    MiddleWareTransactionResponse getRecentTransactionResponse(@PathVariable String customerId);

    @GetExchange("/wallet/transactions/{customerId}")
    MiddleWareTransactionResponse getTransactionResponse(@PathVariable String customerId);
}
