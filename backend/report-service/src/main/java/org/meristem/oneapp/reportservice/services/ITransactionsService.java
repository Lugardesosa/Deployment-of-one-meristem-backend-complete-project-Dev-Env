package org.meristem.oneapp.reportservice.services;


import org.meristem.oneapp.kafka.dtos.TransactionEventDto;
import org.meristem.oneapp.reportservice.domains.requests.TransactionResponse;
import org.meristem.oneapp.reportservice.domains.requests.TransactionsRequest;
import org.meristem.oneapp.reportservice.domains.responses.PageTransactionsResponse;
import org.springframework.data.domain.Page;

public interface ITransactionsService {

    void saveTransactions(TransactionEventDto transactionEventDto);

    Page<PageTransactionsResponse> getTransactions(TransactionsRequest request);

    Page<TransactionResponse> getTransaction(TransactionsRequest request);
}
