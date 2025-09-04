package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record InstrumentResponse(Long id, String name, String code) {
}
