package org.meristem.oneapp.usersservice.integrations.requests;

import lombok.Builder;

@Builder
public record UpdateAddressRequest(
        String customerId,
        String primaryStreet,
        String primaryCity,
        String primaryStateCd,
        String primaryCountryCd,
        String primaryZip,
        String postalAddress,
        String remarks
) {
}
