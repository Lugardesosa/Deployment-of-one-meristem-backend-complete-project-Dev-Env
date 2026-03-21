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
@Table("vendor")
public class Vendor extends BaseModel<String> {

    private String vendorName;
    private String vendorCode;
    private Boolean isLocked;

    @Builder
    public Vendor(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String vendorName, String vendorCode) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.vendorName = vendorName;
        this.vendorCode = vendorCode;
        this.isLocked = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vendor vendor = (Vendor) o;
        return Objects.equals(getId(), vendor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
