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
@Table("user_instrument")
public class UserInstrument extends BaseModel<String> {

    private Long userId;
    private Long instrumentId;
    private Boolean accessed;
    private Boolean kycCompleted;


    @Builder
    public UserInstrument(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId, Long instrumentId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.userId = userId;
        this.instrumentId = instrumentId;
        this.accessed = false;
        this.kycCompleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserInstrument that = (UserInstrument) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getInstrumentId(), that.getInstrumentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getInstrumentId());
    }
}
