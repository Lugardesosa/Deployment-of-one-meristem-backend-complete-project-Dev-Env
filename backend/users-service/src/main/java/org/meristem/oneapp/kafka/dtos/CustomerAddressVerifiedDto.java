package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;

@Builder
public record CustomerAddressVerifiedDto(
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
