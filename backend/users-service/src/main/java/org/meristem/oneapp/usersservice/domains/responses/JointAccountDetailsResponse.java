package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record JointAccountDetailsResponse(

        Long id,

        String accountId,

        String customerId,

        String accountName,

        Integer role,

        Integer mandateType
) {
}
