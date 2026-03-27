package org.meristem.oneapp.usersservice.domains.responses;

public record RequirementResponse(
        Long id,
        String displayName,
        String requirementName) {
}
