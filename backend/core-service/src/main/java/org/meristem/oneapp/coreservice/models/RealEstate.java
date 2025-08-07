package org.meristem.oneapp.coreservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("real_estate")
public class RealEstate extends Assets {

    @NotBlank(message = "Cannot be blank")
    @Column("property_type")
    private String propertyType;

    @NotBlank(message = "Cannot be blank")
    @Size( max = 300, message = "Cannot be more than 300 chars")
    @Column("property_description")
    private String propertyDescription;

    @NotBlank(message = "Cannot be blank")
    @Size( max = 300, message = "Cannot be more than 300 chars")
    @Column("property_address")
    private String propertyAddress;


    @Builder
    public RealEstate(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails, String propertyType, String propertyDescription, String propertyAddress) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, ownerId, estimatedAmount, currencyId, otherDetails);
        this.propertyType = propertyType;
        this.propertyDescription = propertyDescription;
        this.propertyAddress = propertyAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RealEstate that = (RealEstate) o;
        return Objects.equals(getPropertyType(), that.getPropertyType()) && Objects.equals(getPropertyAddress(), that.getPropertyAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPropertyType(), getPropertyAddress());
    }
}
