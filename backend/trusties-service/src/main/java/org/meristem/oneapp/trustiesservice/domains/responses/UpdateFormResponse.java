package org.meristem.oneapp.trustiesservice.domains.responses;

import lombok.Builder;

@Builder
public record UpdateFormResponse(Integer successCount, String message) {
}
