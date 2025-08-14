package org.meristem.oneapp.coreservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record GetPlanResponse(@Schema(oneOf = {NominatedFundResponse.class, SimpleWillResponse.class, ComprehensiveWillResponse.class}, description = "returns a list of one of the following values") List<?> results) {
}
