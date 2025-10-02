package org.meristem.oneapp.trusteesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityLogType {

    CREATED("CREATED", "created"),
    UPDATED("UPDATED", "updated"),
    DELETED("DELETED", "deleted"),
    UPLOADED("UPLOADED", "uploaded"),
    ADDED("ADDED", "added"),
    REMOVED("REMOVED", "removed"),
    VALUED("VALUED", "valued"),
    REQUESTED("REQUESTED", "requested"),
    DEPOSITED("DEPOSITED", "deposited"),
    WITHDREW("WITHDREW", "withdrew");

    private final String action;
    private final String verb;
}
