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

    private Long entityId;
    private String decision;
    private String amlType;
    private String decidedBy;
    private LocalDateTime decidedDate;
    private String reason;
    private String decisionGroupId;

    @Builder
    public AmlDecision(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                       Long entityId, String decision, String decidedBy, LocalDateTime decidedDate, String reason, String decisionGroupId, String amlType) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.entityId = entityId;
        this.decision = decision;
        this.decidedBy = decidedBy;
        this.decidedDate = decidedDate;
        this.reason = reason;
        this.decisionGroupId = decisionGroupId;
        this.amlType = amlType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AmlDecision that = (AmlDecision) o;
        return Objects.equals(getDecisionGroupId(), that.getDecisionGroupId()) && Objects.equals(getEntityId(), that.getEntityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDecisionGroupId(), getEntityId());
    }
}
