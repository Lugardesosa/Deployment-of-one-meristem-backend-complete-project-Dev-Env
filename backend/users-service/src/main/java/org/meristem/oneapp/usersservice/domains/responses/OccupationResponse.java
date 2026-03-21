package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

import java.util.List;

@Builder
public record OccupationResponse(List<Occupation> occupations) {

    public record Occupation(Long id, String name) {

    }
}
