package org.meristem.oneapp.walletservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record BankAccountResponse(String accountName, @JsonIgnore String firstName, @JsonIgnore String lastName, @JsonIgnore String middleName) {
}
