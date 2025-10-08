package org.meristem.oneapp.walletservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record WemaAccountQueryResponse(@JsonProperty("accountname") String accountName, String status, @JsonProperty("status_desc") String statusDesc) {
}