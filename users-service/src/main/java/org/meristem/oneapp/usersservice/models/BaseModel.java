package org.meristem.oneapp.usersservice.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This is the base entity model.
 * @param <U>
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BaseModel<U> implements Serializable {

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
}
