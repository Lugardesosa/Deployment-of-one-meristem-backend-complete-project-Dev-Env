package org.meristem.oneapp.trusteesservice.domains.responses;

import lombok.Builder;

@Builder
public record AssetDeleteResponse(String message, boolean status) {
}
