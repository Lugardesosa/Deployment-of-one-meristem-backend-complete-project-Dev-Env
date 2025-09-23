package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record RiskAppetiteResponse(String riskAppetite, String[] description, String riskProfile) {
}
