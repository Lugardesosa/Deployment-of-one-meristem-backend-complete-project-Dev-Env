package org.meristem.oneapp.trusteesservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@NoArgsConstructor
@Setter
@Getter
@Table("alternate_assets")
public class AlternateAssets extends Assets {

    @NotBlank(message = "Cannot be blank")
    private String assetType;

    @NotBlank(message = "Cannot be blank")
    @Size(max = 150, message = "Must not be more than 150")
    private String platform;

    @Size(max = 300, message = "Must not be more than 300")
    @NotBlank(message = "Cannot be blank")
    // Can be email
    private String uniqueId;

    private String walletAddress;

    @Builder
    public AlternateAssets(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails, String assetType, String platform, String uniqueId, String walletAddress) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, ownerId, estimatedAmount, currencyId, otherDetails);
        this.assetType = assetType;
        this.platform = platform;
        this.uniqueId = uniqueId;
        this.walletAddress = walletAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AlternateAssets that = (AlternateAssets) o;
        return Objects.equals(getUniqueId(), that.getUniqueId()) && Objects.equals(getWalletAddress(), that.getWalletAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId(), getWalletAddress());
    }
}
