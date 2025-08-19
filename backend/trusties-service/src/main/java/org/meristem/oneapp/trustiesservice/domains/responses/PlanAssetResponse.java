package org.meristem.oneapp.trustiesservice.domains.responses;

import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(
        name = "PlanAssetResponse",
        description = "Represents an asset associated with a plan.",
        example = """
          {
            "assetType": "CASH",
            "assetId": 123456789
          }
        """
)
@Builder
public record PlanAssetResponse(
        @Schema(description = "Type/category of the asset.", example = "SECURITY")
        String assetType,
        @Schema(description = "Unique identifier of the asset.", example = "123456789")
        Long assetId) {

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
