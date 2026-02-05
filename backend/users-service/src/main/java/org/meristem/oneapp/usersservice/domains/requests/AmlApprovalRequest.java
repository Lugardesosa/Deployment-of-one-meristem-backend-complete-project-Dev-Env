package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.domains.enums.AmlDecisionType;
import org.meristem.oneapp.usersservice.domains.enums.AmlResultType;

public record AmlApprovalRequest(@NotNull(message = "Cannot be null") Long userId,
                                 @NotNull(message = "Cannot be null") Long searchId,
                                 @NotNull(message = "Cannot be null") AmlDecision pep, @NotNull(message = "Cannot be null") AmlDecision sanction, @NotNull(message = "Cannot be null") AmlDecision adverseMedia) {

    public record AmlDecision(@NotNull(message = "Cannot be null") AmlResultType amlType, @NotNull(message = "Cannot be null") AmlDecisionType amlDecision, @NotNull(message = "Cannot be null") String reason) {

    }
}
