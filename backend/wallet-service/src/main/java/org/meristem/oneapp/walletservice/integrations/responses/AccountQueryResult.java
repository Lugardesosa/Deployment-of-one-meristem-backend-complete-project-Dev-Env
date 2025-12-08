package org.meristem.oneapp.walletservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AccountQueryResult(boolean status, String message, AccountDetails data) {

    public record AccountDetails(@JsonAlias("account_number") String accountNumber, @JsonAlias("account_name") String accountName) {

    }
}
