package org.meristem.oneapp.trustiesservice.models;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Assets extends BaseModel<String> {

    @NotNull(message = "owner assetType cannot be null")
    @Column("owner_id")
    private Long ownerId;

    @NotNull(message = "Cannot be null")
    @Column("estimated_amount")
    private BigDecimal estimatedAmount;

    @NotNull(message = "Cannot be null")
    @Column("currency_id")
    private Long currencyId;

    @Size(max = 500)
    @Column("other_details")
    private String otherDetails;

    public Assets(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.ownerId = ownerId;
        this.estimatedAmount = estimatedAmount;
        this.currencyId = currencyId;
        this.otherDetails = otherDetails;
    }
}
