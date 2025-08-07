package org.meristem.oneapp.coreservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("selections")
public class Selections extends BaseModel<String> {

    @Column("form_id")
    private Long formId;

    @Column("selection_value")
    private String selectionValue;

    @Column("additional_value")
    private String additionalValue;

    @Builder
    public Selections(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long formId, String selectionValue,  String additionalValue) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.formId = formId;
        this.selectionValue = selectionValue;
        this.additionalValue = additionalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Selections that = (Selections) o;
        return Objects.equals(getFormId(), that.getFormId()) && Objects.equals(getSelectionValue(), that.getSelectionValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFormId(), getSelectionValue());
    }
}
