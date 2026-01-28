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

    private String subjectType;
    private String subjectReference;
    private String searchType;
    private String vendor;
    private LocalDateTime completedDate;


    @Builder
    public AmlSearch(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String subjectType, String subjectReference, String searchType, String vendor, LocalDateTime completedDate) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.subjectType = subjectType;
        this.subjectReference = subjectReference;
        this.searchType = searchType;
        this.vendor = vendor;
        this.completedDate = completedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AmlSearch amlSearch = (AmlSearch) o;
        return Objects.equals(getId(), amlSearch.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
