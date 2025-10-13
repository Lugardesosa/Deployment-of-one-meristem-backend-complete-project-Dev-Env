package org.meristem.oneapp.walletservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProvidusAccountFundedEventRequest(
        String event,
        Data data
) {
    public record Data(
            LocalDateTime paidAt,
            BigDecimal amount,
            String narration,

            @JsonAlias("sessionID")
            String sessionId,
            String channelCode,
            String status,
            String beneficiaryAccountName,
            String beneficiaryAccountNumber,
            String type,
            String walletId,
            String userId,
            String beneficiaryBankVerificationNumber,
            String destinationInstitutionCode,
            String originatorAccountName,
            String originatorAccountNumber,
            String originatorBankVerificationNumber,
            String accountName,
            String accountNumber,
            String reference,
            String BankVerificationCode
    ) {}
}