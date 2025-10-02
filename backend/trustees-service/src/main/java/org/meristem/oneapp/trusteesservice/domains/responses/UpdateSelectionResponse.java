package org.meristem.oneapp.trusteesservice.domains.responses;

import lombok.Builder;

@Builder
public record UpdateSelectionResponse(Integer successCount, String message) {
}
