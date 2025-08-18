package org.meristem.oneapp.trustiesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DocumentType {

    IMAGE("IMAGE"),
    DOCUMENT("DOCUMENT");

    private final String value;
}
