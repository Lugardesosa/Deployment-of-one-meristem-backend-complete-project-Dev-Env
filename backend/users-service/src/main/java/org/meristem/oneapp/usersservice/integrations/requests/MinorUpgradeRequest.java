package org.meristem.oneapp.usersservice.integrations.requests;

import lombok.Builder;

@Builder
public record MinorUpgradeRequest(
        String minorCustomerId,
        String minorDateOfBirth,
        String minorNin,
        String minorBvn,
        String minorEmailAddress,
        String minorAddressCountryCode,
        String minorAddressStateCode,
        String minorAddressCity,
        String minorPhoneNumber,
        String parentCustomerId,
        String parentBvn,
        String remark
) {
}
