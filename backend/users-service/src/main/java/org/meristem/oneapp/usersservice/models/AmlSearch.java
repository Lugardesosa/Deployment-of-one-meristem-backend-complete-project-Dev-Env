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
@Table("aml_search")
public class AmlSearch extends BaseModel<String> {

    private Long entityId;
    private String clientSearchId;

    @Builder
    public AmlSearch(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long entityId, String clientSearchId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.entityId = entityId;
        this.clientSearchId = clientSearchId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AmlSearch amlVendor = (AmlSearch) o;
        return Objects.equals(getId(), amlVendor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
