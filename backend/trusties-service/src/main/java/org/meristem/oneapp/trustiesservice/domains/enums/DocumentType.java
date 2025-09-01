package org.meristem.oneapp.trustiesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DocumentType {

    IMAGE("IMAGE", 0),
    DOCUMENT("DOCUMENT", 1);

    private final String value;
    private final int index;
}
