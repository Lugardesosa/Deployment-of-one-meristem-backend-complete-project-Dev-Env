package org.meristem.oneapp.trustiesservice.domains.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Builder
public record GetAssetValueResponse(List<EstimatedValueDetails> details) {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EstimatedValueDetails {

        private Long currencyId;
        private String currencyLogo;
        private BigDecimal value;
        @Builder.Default
        private List<AssetEstimatedValueDetails> assetEstimatedValueDetails = new ArrayList<>();

        @Builder
        public record AssetEstimatedValueDetails(BigDecimal value, String table) {
        }
    }
}
