package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Roles {

    USER(0, "User"),
    ADMIN(1, "Admin"),
    COMPLIANCE_OFFICER(2, "Compliance Officer"),
    INVESTMENT_OFFICER(3, "Investment Officer"),
    SYSTEM_ADMIN(4, "System Admin"),
    FINANCE_OFFICER(5, "Finance Officer"),
    AUDITOR(6, "Auditor"),
    TRUST_OFFICER(7, "Trust Officer"),
    LEGAL_OFFICER(8, "Legal Officer");

    private final Integer value;
    private final String name;

    public static List<String> getAdminRoles() {

        return Arrays.stream(Roles.values()).filter(r -> r != Roles.USER).map(Enum::name).toList();
    }
}