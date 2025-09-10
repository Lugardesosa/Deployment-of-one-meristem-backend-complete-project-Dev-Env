package org.meristem.oneapp.trustiesservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityLogNote {

    NEW_ASSET_ADDED("Added a New Asset; %s"),
    NEW_BENEFICIARY_ADDED("%d New %s Added To A %s"),
    ASSET_REMOVED("%d %s Removed From %s"),
    BENEFICIARY_REMOVED("%d %s Removed From %s"),
    AMOUNT_ADDED_TRUST("%f Added to %s Trust"),
    WILL_EXECUTOR_ADDED("%d Will %s Added to %s"),
    WILL_EXECUTOR_REMOVED("%d Will %s Removed From %s"),
    VALUATION_STATEMENT_DOWNLOADED("Downloaded Valuation Statement"),
    ASSET_VALUE_UPDATED("Updated Asset Value; %s"),
    ASSET_ASSIGNED("%d %s Assigned To %s"),

    // Asset Categories
    ASSET_CATEGORY_CREATED("Asset Category ‘%s’ Created - %s"),
    ASSET_CATEGORY_REMOVED("Removed Asset Category ‘%s’"),

    // Wills & Trusts
    WILL_CREATED("Created %s"),
    WILL_REMOVED("Removed %s");

    private final String description;
}
