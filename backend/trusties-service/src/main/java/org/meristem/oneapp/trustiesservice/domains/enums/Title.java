package org.meristem.oneapp.trustiesservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Title {

    MR("MR"),
    MRS("MRS"),
    MS("MS");

    private final String displayName;
}
