package org.meristem.oneapp.coreservice.domains.responses;

import lombok.Builder;

@Builder
public record UpdateFormResponse(Integer successCount, String message) {
}
