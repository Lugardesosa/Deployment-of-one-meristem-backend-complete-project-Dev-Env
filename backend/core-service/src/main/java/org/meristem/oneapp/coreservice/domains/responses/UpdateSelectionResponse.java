package org.meristem.oneapp.coreservice.domains.responses;

import lombok.Builder;

@Builder
public record UpdateSelectionResponse(Integer successCount, String message) {
}
