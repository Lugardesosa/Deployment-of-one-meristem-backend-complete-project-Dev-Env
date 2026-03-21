package org.meristem.oneapp.trusteesservice.models;


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
@Setter
@Getter
@Table("intellectual_property")
public class IntellectualProperty extends Assets {

    @NotBlank(message = "Cannot be blank")
    @Column("property_type")
    private String propertyType;

    @NotBlank(message = "Cannot be blank")
    @Column("registered_name")
    @Size(max = 300, message = "Cannot be more than 300 chars")
    private String registeredName;

    @NotBlank(message = "Cannot be blank")
    @Size(max = 500, message = "Cannot be more than 500 chars")
    @Column("property_description")
    private String propertyDescription;

    @Builder
    public IntellectualProperty(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails, String propertyType, String registeredName, String propertyDescription) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, ownerId, estimatedAmount, currencyId, otherDetails);
        this.propertyType = propertyType;
        this.registeredName = registeredName;
        this.propertyDescription = propertyDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IntellectualProperty that = (IntellectualProperty) o;
        return Objects.equals(getPropertyType(), that.getPropertyType()) && Objects.equals(getRegisteredName(), that.getRegisteredName()) && Objects.equals(getPropertyDescription(), that.getPropertyDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPropertyType(), getRegisteredName(), getPropertyDescription());
    }
}
