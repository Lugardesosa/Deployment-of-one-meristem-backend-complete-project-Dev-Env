package org.meristem.oneapp.walletservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record WemaTransactionResponse(@JsonProperty("transactionreference") String transactionReference, String status, @JsonProperty("status_desc") String statusDesc) {
}
