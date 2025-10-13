package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SignedUrlType {

    IMAGE(0),
    DOCUMENT(1);

    private final int value;
}
