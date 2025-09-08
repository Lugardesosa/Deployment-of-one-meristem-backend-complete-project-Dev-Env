package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

import java.util.List;

@Builder
public record RiskAppetiteResponse(String riskAppetite, String[] description, String riskProfile) {
}
