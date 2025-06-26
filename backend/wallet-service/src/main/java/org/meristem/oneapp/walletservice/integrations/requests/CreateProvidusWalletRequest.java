package org.meristem.oneapp.walletservice.integrations.requests;

import lombok.Builder;

import java.util.Map;

@Builder
public record CreateProvidusWalletRequest(
        String bvn,
        String firstName,
        String lastName,
        String dateOfBirth,
        String phoneNumber,
        String email,
        String address,
        Map<String, String> metadata
) {}