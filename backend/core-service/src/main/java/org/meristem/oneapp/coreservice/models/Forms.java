package org.meristem.oneapp.coreservice.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table("forms")
public class Forms extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    @Column("form_position")
    @Size(max = 255, message = "Cannot be more than 255 chars")
    private Long formPosition;

    @Column("placeholder")
    @Size(max = 255, message = "Cannot be more than 255 chars")
    private String placeholder;

    @Column("subtext")
    @Size(max = 255, message = "Cannot be more than 255 chars")
    private String subtext;

    @NotNull(message = "Cannot be null")
    @Column("type")
    @Size(max = 100, message = "Cannot be more than 255 chars")
    private String type;

    @NotNull(message = "Cannot be null")
    @Column("field_order")
    private Integer order;

    @NotNull(message = "Cannot be null")
    @Column("label")
    @Size(max = 255, message = "Cannot be more than 255 chars")
    private String label;

    @Column("default_value")
    @Size(max = 300, message = "Cannot be more than 300 chars")
    private String defaultValue;

    @Column("text_size")
    private Integer textSize;

    @NotNull(message = "Cannot be null")
    @Column("mandatory")
    private Integer mandatory;

    @Builder
    public Forms(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long formPosition,
                 String placeholder, String type, Integer order, String label, Integer mandatory, Integer textSize, String defaultValue, String subtext) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.formPosition = formPosition;
        this.placeholder = placeholder;
        this.type = type;
        this.order = order;
        this.label = label;
        this.mandatory = mandatory;
        this.textSize = textSize;
        this.defaultValue = defaultValue;
        this.subtext = subtext;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Forms forms = (Forms) o;
        return Objects.equals(getId(), forms.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOrder());
    }
}
