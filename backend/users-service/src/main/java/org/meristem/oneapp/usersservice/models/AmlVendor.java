package org.meristem.oneapp.usersservice.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("aml_vendor")
public class AmlVendor extends BaseModel<String> {

    private String vendorName;
    private String vendorCode;

    @Builder
    public AmlVendor(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String vendorName, String vendorCode) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.vendorName = vendorName;
        this.vendorCode = vendorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AmlVendor amlVendor = (AmlVendor) o;
        return Objects.equals(getId(), amlVendor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
