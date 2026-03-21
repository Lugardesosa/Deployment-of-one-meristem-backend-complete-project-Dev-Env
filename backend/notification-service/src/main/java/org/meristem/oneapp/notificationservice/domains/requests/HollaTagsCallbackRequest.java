package org.meristem.oneapp.notificationservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record HollaTagsCallbackRequest(

        @JsonProperty("message_id")
        String messageId,
        String status,
        @JsonProperty("status_code")
        String statusCode,
        @JsonProperty("done_date")
        OffsetDateTime doneDate,
        String operator,
        int length,
        int page,
        BigDecimal cost
) {
}
