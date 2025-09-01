package org.meristem.oneapp.trustiesservice.domains.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContributionFrequency {
    DAILY(1, "Daily"),
    WEEKLY(2, "Weekly"),
    MONTHLY(3, "Monthly"),
    BI_MONTHLY(4, "Bi-monthly"),
    ANNUALLY(5, "Annually"),
    OTHER(6, "");

    private final int value;
    private final String displayName;

    public static ContributionFrequency fromValue(int value) {
        for (ContributionFrequency obj : ContributionFrequency.values()) {
            if (obj.value == value) {
                return obj;
            }
        }
        return OTHER;
    }
}
