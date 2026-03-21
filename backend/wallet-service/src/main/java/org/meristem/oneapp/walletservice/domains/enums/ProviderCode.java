package org.meristem.oneapp.walletservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProviderCode {
    PROVIDUS("PROVIDUS"),
    PAYSTACK("PAYSTACK");

    private final String value;
}