package org.meristem.oneapp.trusteesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Response objects for plans")
public record GetPlanResponse(@Schema(description = "returns a list of one of the following values") List<?> results) {
}
