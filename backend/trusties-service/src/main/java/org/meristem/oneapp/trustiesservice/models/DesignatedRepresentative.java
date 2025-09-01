package org.meristem.oneapp.trustiesservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@Table("designated_representative")
public class DesignatedRepresentative extends BaseModel<String> {

    @NotNull(message = "Not blank")
    private Long ownerId;

    @NotBlank(message = "Cannot be null")
    @Size(min = 1, max = 300)
    private String representativeName;

    @Size(min = 1, max = 300)
    @NotBlank(message = "Cannot be null")
    private String representativeAddress;

    @Size(min = 1, max = 300)
    @NotBlank(message = "Cannot be null")
    private String representativeEmail;

    @Size(min = 1, max = 50)
    @NotBlank(message = "Cannot be null")
    private String representativePhoneNumber;

    @NotNull(message = "Not blank")
    private Long planId;

    @Builder
    public DesignatedRepresentative(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, String representativeName, String representativeAddress, String representativeEmail, String representativePhoneNumber, Long planId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.ownerId = ownerId;
        this.representativeName = representativeName;
        this.representativeAddress = representativeAddress;
        this.representativeEmail = representativeEmail;
        this.representativePhoneNumber = representativePhoneNumber;
        this.planId = planId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DesignatedRepresentative that = (DesignatedRepresentative) o;
        return Objects.equals(getOwnerId(), that.getOwnerId()) && Objects.equals(getRepresentativeEmail(), that.getRepresentativeEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwnerId(), getRepresentativeEmail());
    }
}
