package org.meristem.oneapp.walletservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OutboxStatus {

    SENT(0), PENDING(1), FAILED(2);

    private final Integer value;
}
