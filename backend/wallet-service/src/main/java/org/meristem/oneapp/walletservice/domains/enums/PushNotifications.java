package org.meristem.oneapp.walletservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PushNotifications {

    TRANSACTION_NOTIFICATION("Transaction notification", "%s has been deposited into your wallet.");

    private final String title;
    private final String body;
}
