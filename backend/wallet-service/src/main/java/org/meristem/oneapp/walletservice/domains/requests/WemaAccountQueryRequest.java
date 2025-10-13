package org.meristem.oneapp.walletservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonAlias;

public record WemaAccountQueryRequest(@JsonAlias("accountnumber") String accountNumber) {
}
