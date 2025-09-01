package org.meristem.oneapp.trustiesservice.domains.responses;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record GetAssetValueResponse(List<EstimatedValueDetails> details) {

    @Builder
    public record EstimatedValueDetails(Long currencyId, String currencyLogo, BigDecimal value) {

    }
}
