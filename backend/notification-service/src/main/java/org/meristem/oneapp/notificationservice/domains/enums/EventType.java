package org.meristem.oneapp.notificationservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventType {
    TRANSACTION,
    PAYOUT
}
