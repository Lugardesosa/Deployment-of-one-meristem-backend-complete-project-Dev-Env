package org.meristem.oneapp.usersservice.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Table("aml_decision")
public class AmlDecision extends BaseModel<String> {

    private Long searchId;
    private Long resultId;
    private String decision;
    private String decidedBy;
    private String reason;

    @Builder
    public AmlDecision(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long searchId, Long resultId, String decision, String decidedBy, String reason) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.searchId = searchId;
        this.resultId = resultId;
        this.decision = decision;
        this.decidedBy = decidedBy;
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AmlDecision that = (AmlDecision) o;
        return Objects.equals(getSearchId(), that.getSearchId()) && Objects.equals(getResultId(), that.getResultId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSearchId(), getResultId());
    }
}
