package org.meristem.oneapp.usersservice.integrations.requests;

import lombok.Builder;

@Builder
public record PastelAmlRequest(String name, Integer threshold, Integer limit, String callbackUrl) {
}
