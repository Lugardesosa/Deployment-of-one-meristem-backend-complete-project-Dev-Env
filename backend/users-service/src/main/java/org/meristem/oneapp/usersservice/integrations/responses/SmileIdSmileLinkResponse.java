package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;

@Builder
public record SmileIdSmileLinkResponse(String link, @JsonAlias("ref_id") String refId) {
}
