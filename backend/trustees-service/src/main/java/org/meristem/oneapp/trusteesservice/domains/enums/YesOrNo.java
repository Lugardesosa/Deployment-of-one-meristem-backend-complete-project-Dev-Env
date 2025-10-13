package org.meristem.oneapp.trusteesservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum YesOrNo {

    YES("Yes"),
    NO("No");
    private final String value;
}
