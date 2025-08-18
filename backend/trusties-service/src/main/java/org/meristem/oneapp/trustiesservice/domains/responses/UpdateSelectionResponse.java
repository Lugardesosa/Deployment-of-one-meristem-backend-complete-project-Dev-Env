package org.meristem.oneapp.trustiesservice.domains.responses;

import lombok.Builder;

@Builder
public record UpdateSelectionResponse(Integer successCount, String message) {
}
