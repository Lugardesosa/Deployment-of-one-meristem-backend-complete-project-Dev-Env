package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;
import org.meristem.oneapp.usersservice.dtos.AdverseMediaDto;
import org.meristem.oneapp.usersservice.dtos.PepDto;
import org.meristem.oneapp.usersservice.dtos.SanctionDto;

import java.util.List;

@Builder
public record AmlResponse(Long userId, List<AdverseMediaDto> adverseMediaDtos, List<PepDto> pepDtos, List<SanctionDto> sanctionDtos) {
}
