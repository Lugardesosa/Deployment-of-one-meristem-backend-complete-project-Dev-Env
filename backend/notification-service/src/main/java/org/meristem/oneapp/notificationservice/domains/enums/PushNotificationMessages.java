package org.meristem.oneapp.notificationservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PushNotificationMessages {

    TRANSFER_SUCCESSFUL("Transfer successful", "A {} {} payment has been made to your wallet.");

    private final String title;
    private final String body;
}
