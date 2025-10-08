package org.meristem.oneapp.trusteesservice.domains.responses;

import lombok.Builder;

@Builder
public record UpdateFormResponse(Integer successCount, String message) {
}
