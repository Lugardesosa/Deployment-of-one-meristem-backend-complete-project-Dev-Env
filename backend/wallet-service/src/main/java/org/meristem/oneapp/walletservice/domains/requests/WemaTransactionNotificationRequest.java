package org.meristem.oneapp.walletservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;

public record WemaTransactionNotificationRequest(
        @JsonAlias("originatoraccountnumber")
        String originatorAccountNumber,

        @JsonAlias("amount")
        BigDecimal amount,

        @JsonAlias("originatorname")
        String originatorName,

        @JsonAlias("narration")
        String narration,

        @JsonAlias("craccountname")
        String creditAccountName,

        @JsonAlias("paymentreference")
        String paymentReference,

        @JsonAlias("bankname")
        String sendersBankName,

        @JsonAlias("sessionid")
        String sessionId,

        @JsonAlias("craccount")
        String creditAccount,

        @JsonAlias("bankcode")
        String sendersBankCode
) {
}
