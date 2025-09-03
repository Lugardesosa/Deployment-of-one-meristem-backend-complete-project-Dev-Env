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
@Table("instrument_accessed")
public class InstrumentAccessed extends BaseModel<String> {

    private Long userId;
    private Long instrumentId;
    private Boolean accessed;

    @Builder
    public InstrumentAccessed(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId, Long instrumentId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.userId = userId;
        this.instrumentId = instrumentId;
        this.accessed = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentAccessed that = (InstrumentAccessed) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getInstrumentId(), that.getInstrumentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getInstrumentId());
    }
}
