package org.meristem.oneapp.usersservice.models;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
@Table("aml_result")
@ToString
public class AmlResult extends BaseModel<String> {

    private Long searchId;

    private Long vendorId;

    private String entityType;
    private Long entityId;

    private JsonNode vendorDataset;

    private String resultType;
    private String vendorReference;

    @Builder
    public AmlResult(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long vendorId, Long searchId,
                     String entityType, Long entityId, JsonNode vendorDataset, String vendorReference, String resultType) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.vendorId = vendorId;
        this.searchId = searchId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.vendorDataset = vendorDataset;
        this.vendorReference = vendorReference;
        this.resultType = resultType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AmlResult amlResult = (AmlResult) o;
        return Objects.equals(getId(), amlResult.getId()) && Objects.equals(getVendorId(), amlResult.getVendorId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVendorId());
    }
}
