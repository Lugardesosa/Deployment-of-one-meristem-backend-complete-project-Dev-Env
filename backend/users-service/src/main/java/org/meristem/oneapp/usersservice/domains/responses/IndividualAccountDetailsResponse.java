package org.meristem.oneapp.usersservice.domains.responses;

public record IndividualAccountDetailsResponse(
        Long userId,

        String customerId
) {
}
