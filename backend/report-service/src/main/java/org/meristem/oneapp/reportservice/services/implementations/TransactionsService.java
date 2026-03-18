package org.meristem.oneapp.reportservice.services.implementations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.TransactionEventDto;
import org.meristem.oneapp.reportservice.constants.AppConstants;
import org.meristem.oneapp.reportservice.domains.enums.ActivityType;
import org.meristem.oneapp.reportservice.domains.enums.TransactionDirection;
import org.meristem.oneapp.reportservice.domains.enums.TransactionSubject;
import org.meristem.oneapp.reportservice.domains.requests.TransactionResponse;
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
import org.meristem.oneapp.reportservice.services.ITransactionsService;
import org.meristem.oneapp.reportservice.utils.AppUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionsService implements ITransactionsService {


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
//            filters.put("type", TransactionType.fromValue(request.getTransactionType()));
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

    @Override
    public Page<TransactionResponse> getTransaction(TransactionsRequest request) {

        PageRequest pageRequest = PageRequest.of(1, 20, Sort.by(request.getSortOrder(), String.join(",", request.getSortBy())));

        if (TransactionSubject.WEALTH.equals(request.getTransactionSubject())) {
            List<TransactionResponse> transactionResponseList = new ArrayList<>();
            BigDecimal oldMoney = BigDecimal.ZERO;
            BigDecimal amount = generateRandomBigDecimalFromRange(new BigDecimal("1000"), new BigDecimal("10000000000"), 2);

            List<TransactionResponse> transactions = List.of(

                    TransactionResponse.builder()
                            .id(1L)
                            .createdDate(LocalDateTime.now().minusMinutes(10))
                            .createdBy("system")
                            .lastModifiedDate(LocalDateTime.now().minusMinutes(10))
                            .lastModifiedBy("system")
                            .version(0)
                            .status(1)
                            .walletId(12L)
                            .userId(1001L)
                            .activityType(ActivityType.DEPOSIT)
                            .transactionDirection(TransactionDirection.CREDIT)
                            .transactionSubject(TransactionSubject.WEALTH)
                            .amount(new BigDecimal("50000.00"))
                            .currency("NGN")
                            .reference(UUID.randomUUID().toString())
                            .transactionDate(LocalDateTime.now().minusMinutes(10))
                            .previousBalance(new BigDecimal("150000.00"))
                            .newBalance(new BigDecimal("200000.00"))
                            .build(),

                    TransactionResponse.builder()
                            .id(2L)
                            .createdDate(LocalDateTime.now().minusMinutes(8))
                            .createdBy("system")
                            .lastModifiedDate(LocalDateTime.now().minusMinutes(8))
                            .lastModifiedBy("system")
                            .version(0)
                            .status(1)
                            .walletId(12L)
                            .userId(1001L)
                            .activityType(ActivityType.BUY)
                            .transactionDirection(TransactionDirection.DEBIT)
                            .transactionSubject(TransactionSubject.WEALTH)
                            .amount(new BigDecimal("30000.00"))
                            .currency("NGN")
                            .reference(UUID.randomUUID().toString())
                            .transactionDate(LocalDateTime.now().minusMinutes(8))
                            .previousBalance(new BigDecimal("200000.00"))
                            .newBalance(new BigDecimal("170000.00"))
                            .build(),

                    TransactionResponse.builder()
                            .id(3L)
                            .createdDate(LocalDateTime.now().minusMinutes(5))
                            .createdBy("system")
                            .lastModifiedDate(LocalDateTime.now().minusMinutes(5))
                            .lastModifiedBy("system")
                            .version(0)
                            .status(1)
                            .walletId(12L)
                            .userId(1001L)
                            .activityType(ActivityType.INTEREST)
                            .transactionDirection(TransactionDirection.CREDIT)
                            .transactionSubject(TransactionSubject.WEALTH)
                            .amount(new BigDecimal("2500.50"))
                            .currency("NGN")
                            .reference(UUID.randomUUID().toString())
                            .transactionDate(LocalDateTime.now().minusMinutes(5))
                            .previousBalance(new BigDecimal("170000.00"))
                            .newBalance(new BigDecimal("172500.50"))
                            .build(),

                    TransactionResponse.builder()
                            .id(4L)
                            .createdDate(LocalDateTime.now().minusMinutes(3))
                            .createdBy("system")
                            .lastModifiedDate(LocalDateTime.now().minusMinutes(3))
                            .lastModifiedBy("system")
                            .version(0)
                            .status(1)
                            .walletId(12L)
                            .userId(1001L)
                            .activityType(ActivityType.WITHDRAW)
                            .transactionDirection(TransactionDirection.DEBIT)
                            .transactionSubject(TransactionSubject.WEALTH)
                            .amount(new BigDecimal("20000.00"))
                            .currency("NGN")
                            .reference(UUID.randomUUID().toString())
                            .transactionDate(LocalDateTime.now().minusMinutes(3))
                            .previousBalance(new BigDecimal("172500.50"))
                            .newBalance(new BigDecimal("152500.50"))
                            .build(),

                    TransactionResponse.builder()
                            .id(5L)
                            .createdDate(LocalDateTime.now().minusMinutes(1))
                            .createdBy("system")
                            .lastModifiedDate(LocalDateTime.now().minusMinutes(1))
                            .lastModifiedBy("system")
                            .version(0)
                            .status(1)
                            .walletId(12L)
                            .userId(1001L)
                            .activityType(ActivityType.DIVIDEND)
                            .transactionDirection(TransactionDirection.CREDIT)
                            .transactionSubject(TransactionSubject.WEALTH)
                            .amount(new BigDecimal("7500.00"))
                            .currency("NGN")
                            .reference(UUID.randomUUID().toString())
                            .transactionDate(LocalDateTime.now().minusMinutes(1))
                            .previousBalance(new BigDecimal("152500.50"))
                            .newBalance(new BigDecimal("160000.50"))
                            .build()
            );

            return new PageImpl<>(transactions, pageRequest, transactions.size());
        }
        return null;
    }

    public static BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max, int scale) {
        BigDecimal range = max.subtract(min);
        BigDecimal randomBigDecimal = min.add(range.multiply(new BigDecimal(Math.random())));
        return randomBigDecimal.setScale(scale, RoundingMode.HALF_EVEN);
    }
}
