package org.meristem.oneapp.usersservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * users 1000
 * admin 2000
 * system 3000
 * compliance 4000
 * investment 5000
 * legal 6000
 * finance 7000
 * audit 8000
 * additional 9000
 */
@AllArgsConstructor
@Getter
public enum PermissionsEnum {

    USER("users", ""),
    ADMIN("admin", ""),
    SYSTEM_ADMIN("system", ""),
    COMPLIANCE_OFFICER("compliance", ""),
    INVESTMENT_OFFICER("investment", ""),
    LEGAL_OFFICER("legal", ""),
    FINANCE_OFFICER("finance", ""),
    AUDITOR("audit", ""),
    TRUST_OFFICER("trust", ""),
    ADDITIONAL("additional", "9000");

    private final String startsWith;
    private final String code;
}
