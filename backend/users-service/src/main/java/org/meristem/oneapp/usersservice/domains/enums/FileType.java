package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileType {

    AVATAR(1),
    PROFILE_PICTURE(2),
    DOCUMENT(2);

    private final int value;
}
