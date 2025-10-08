package org.meristem.oneapp.reportservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.TransactionEventDto;
import org.meristem.oneapp.reportservice.constants.AppConstants;
import org.meristem.oneapp.reportservice.domains.enums.TransactionType;
import org.meristem.oneapp.reportservice.domains.requests.TransactionsRequest;
import org.meristem.oneapp.reportservice.domains.responses.PageTransactionsResponse;
import org.meristem.oneapp.reportservice.domains.responses.TransactionsResponse;
import org.meristem.oneapp.reportservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.reportservice.mappers.TransactionsMapper;
import org.meristem.oneapp.reportservice.models.Transactions;
import org.meristem.oneapp.reportservice.models.TransactionsMetadata;
import org.meristem.oneapp.reportservice.repositories.GeneralRepository;
import org.meristem.oneapp.reportservice.repositories.TransactionMetadataRepository;
import org.meristem.oneapp.reportservice.repositories.TransactionsRepository;
import org.meristem.oneapp.reportservice.utils.AppUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionsService {


    private final TransactionsMapper transactionsMapper = TransactionsMapper.INSTANCE;
    private final TransactionsRepository transactionsRepository;
    private final GeneralRepository generalRepository;
    private final TransactionMetadataRepository transactionMetadataRepository;

    public void saveTransactions(TransactionEventDto transactionEventDto) {

        Transactions transactions = transactionsMapper.transactionEventDtoToTransactions(transactionEventDto);
        List<TransactionsMetadata> metadata = new ArrayList<>();
        transactions = transactionsRepository.save(transactions);

        for (Map.Entry<String, Object> entry : transactionEventDto.metadata().entrySet()) {
            metadata.add(TransactionsMetadata.builder().transactionId(transactions.getId()).key(entry.getKey())
                    .value(String.valueOf(entry.getValue())).build());
        }
        transactionMetadataRepository.saveAll(metadata);
    }

    public Page<PageTransactionsResponse> getTransactions(TransactionsRequest request) {

        Map<String, Object> filters = new HashMap<>();

        if (nonNull(request.getReference())) {
            filters.put("reference", request.getReference());
        } else {
            if (!AppUtil.isValidDateRage(request.getFrom(), AppConstants.DAYS_RANGE)) {
                throw new BadRequestException(String.format("Invalid from/to date, not more than %d days or 12 months", AppConstants.DAYS_RANGE));
            }
            filters.put("providerTransactionDate", Pair.of(request.getFrom(), request.getTo()));
            filters.put("transactionStatus", request.getTransactionStatus());
            filters.put("type", TransactionType.fromValue(request.getTransactionType()));
        }

        request.setSortBy(Collections.singletonList("providerTransactionDate"));
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), Sort.by(request.getSortOrder(), String.join(",", request.getSortBy())));

        Page<Transactions> transactions = generalRepository.findAllBy(Transactions.class, filters, pageRequest);


        List<TransactionsResponse> transactionsResponses = transactionsMapper.transactionsToTransactionsResponse(transactions.getContent());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        List<PageTransactionsResponse> page = transactionsResponses.stream()
                .collect(Collectors.groupingBy(p -> p.providerTransactionDate().toLocalDate().format(formatter),
                        LinkedHashMap::new, Collectors.toList())).entrySet().stream().map(entry ->
                        new PageTransactionsResponse(entry.getKey(), entry.getValue())).toList();
        return new PageImpl<>(page, pageRequest, transactions.getTotalElements());
    }
}
