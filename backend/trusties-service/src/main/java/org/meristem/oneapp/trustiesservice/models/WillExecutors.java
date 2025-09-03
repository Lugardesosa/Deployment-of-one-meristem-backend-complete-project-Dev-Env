package org.meristem.oneapp.trustiesservice.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table("will_executors")
public class WillExecutors extends BaseModel<String> {

    @NotNull(message = "Not blank")
    private Long ownerId;

    @NotBlank(message = "Cannot be null")
    @Size(min = 1, max = 300)
    private String willExecutorName;

    @Size(min = 1, max = 300)
    @NotBlank(message = "Cannot be null")
    private String willExecutorAddress;

    @NotNull(message = "Not blank")
    private Long planId;

    // Plans enum
    @NotBlank(message = "Not blank")
    private String planType;

    @Builder
    public WillExecutors(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, String willExecutorName, String willExecutorAddress, Long planId, String planType) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.ownerId = ownerId;
        this.willExecutorName = willExecutorName;
        this.willExecutorAddress = willExecutorAddress;
        this.planId = planId;
        this.planType = planType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WillExecutors that = (WillExecutors) o;
        return Objects.equals(getOwnerId(), that.getOwnerId()) && Objects.equals(getWillExecutorName(), that.getWillExecutorName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwnerId(), getWillExecutorName());
    }
}
