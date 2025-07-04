package org.meristem.oneapp.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionEventDto(
        LocalDateTime createdDate,
        Long walletId,
        Long virtualAccountId,
        String reference,
        String providerReference,
        BigDecimal amount,
        BigDecimal previousBalance,
        BigDecimal newBalance,
        String type,
        String method,
        String narration,
        Map<String, Object> metadata,
        String sendersBankName,
        String sendersBankCode,
        String sendersAccountNumber,
        String sendersName,
        Integer status,
        String accountNumber,
        String accountName,
        LocalDateTime transactionDate

) {

    public TransactionEventDto withers(TransactionEventDto dto, String accountNumber, String accountName, String type, String method, Map<String, Object> metadata) {

        return TransactionEventDto.builder()
                .accountName(accountName).accountNumber(accountNumber).type(type).walletId(dto.walletId()).method(method)
                .virtualAccountId(dto.virtualAccountId()).reference(dto.reference()).providerReference(dto.providerReference())
                .amount(dto.amount()).previousBalance(dto.previousBalance()).newBalance(dto.newBalance()).createdDate(dto.createdDate())
                .metadata(metadata).narration(dto.narration()).sendersBankName(dto.sendersBankName()).sendersBankCode(dto.sendersBankCode())
                .sendersAccountNumber(dto.sendersAccountNumber()).sendersName(dto.sendersName())
                .status(dto.status()).transactionDate(dto.transactionDate()).build();
    }
}
