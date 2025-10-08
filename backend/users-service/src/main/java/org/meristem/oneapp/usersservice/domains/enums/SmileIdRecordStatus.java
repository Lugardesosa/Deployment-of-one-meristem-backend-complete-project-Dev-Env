package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SmileIdRecordStatus {

    APPROVED(0),
    FAILED(0);

    private final Integer value;
}
