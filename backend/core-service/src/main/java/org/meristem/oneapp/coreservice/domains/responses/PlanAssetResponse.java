package org.meristem.oneapp.coreservice.domains.responses;

import lombok.Builder;

import java.util.Objects;

@Builder
public record PlanAssetResponse(String assetType, Long assetId) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlanAssetResponse that = (PlanAssetResponse) o;
        return Objects.equals(assetType(), that.assetType()) && Objects.equals(assetId(), that.assetId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(assetType(), assetId());
    }
}
