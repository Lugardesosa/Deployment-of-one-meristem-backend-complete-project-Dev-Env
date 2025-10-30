package org.meristem.oneapp.trusteesservice.models;


import jakarta.validation.constraints.NotBlank;
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
@Table("personal_assets")
public class PersonalAssets extends Assets {

    @NotBlank(message = "Cannot be blank")
    private String assetType;

    @NotBlank(message = "Cannot be blank")
    private String assetDescription;

    @NotBlank(message = "Cannot be blank")
    private String identifyingNo;

    @Builder
    public PersonalAssets(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails, String assetType, String assetDescription, String identifyingNo) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, ownerId, estimatedAmount, currencyId, otherDetails);
        this.assetType = assetType;
        this.assetDescription = assetDescription;
        this.identifyingNo = identifyingNo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PersonalAssets that = (PersonalAssets) o;
        return Objects.equals(getAssetType(), that.getAssetType()) && Objects.equals(getIdentifyingNo(), that.getIdentifyingNo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAssetType(), getIdentifyingNo());
    }
}
