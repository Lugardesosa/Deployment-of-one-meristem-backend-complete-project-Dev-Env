package org.meristem.oneapp.notificationservice.integrations.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HollaTagsSmsRequest(
        String user, String pass, String from,
        String to, // comma separated internation numbers without the '+'. Not more than 500 numbers per request
        String msg, int type,
        @JsonProperty("callback_url") String callbackUrl,
        @JsonProperty("message_uuid") String messageUuid) {
}
