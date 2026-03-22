package org.meristem.oneapp.coreservices.notifications.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.meristem.oneapp.coreservices.notifications.domains.enums.EntityStatus;
import org.springframework.data.annotation.*;

import java.time.LocalDateTime;


/**
 * This is the base entity model.
 * @param <U>
 */
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class BaseModel<U> {

    @Id
    @NotNull(message = "id cannot be null")
    private Long id;

    @CreatedDate
    @NotNull(message = "createdDate cannot be null")
    private LocalDateTime createdDate;

    @CreatedBy
    @NotNull(message = "createdBy cannot be null")
    private U createdBy;

    @LastModifiedDate
    @NotNull(message = "lastModifiedDate cannot be null")
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @NotNull(message = "lastModifiedBy cannot be null")
    private U lastModifiedBy;

    @JsonIgnore
    @Version
    @NotNull(message = "version cannot be null")
    private Integer version;

    @NotNull(message = "Cannot be null")
    private Integer status;

    public BaseModel(Long id, LocalDateTime createdDate, U createdBy, LocalDateTime lastModifiedDate, U lastModifiedBy, Integer version) {
        this.id = id;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedBy = lastModifiedBy;
        this.version = version;
        this.status = EntityStatus.ACTIVE.getValue();
    }
}
