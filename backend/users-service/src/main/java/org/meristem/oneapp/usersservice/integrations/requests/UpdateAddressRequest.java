package org.meristem.oneapp.usersservice.integrations.requests;

import lombok.Builder;

@Builder
public record UpdateAddressRequest(
        String addressStreet,
        String addressCity,
        String addressStateCd,
        String addressCountryCd,
        String addressZipCode
) {
}
