package org.meristem.oneapp.usersservice.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@NoArgsConstructor
@Setter
@Getter
@Table("id_card")
public class IdCard extends BaseModel<String> {

    @Column("id_card_type")
    @NotBlank(message = "Cannot be null")
    String idCardType;

    @Column("id_value")
    @NotBlank(message = "cannot be null") @Size(min = 5, max = 30, message = "cannot be longer than 20 and less than 9")
    String idValue;

    @Column("issued_date")
    String issuedDate;

    @Column("expiry_date")
    String expiryDate;

    @Column("user_id")
    @NotNull(message = "User id cannot be null")
    private Long userId;

    @Builder
    public IdCard(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                  String idCardType, String idValue, String issuedDate, String expiryDate, Long userId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.idCardType = idCardType;
        this.idValue = idValue;
        this.issuedDate = issuedDate;
        this.expiryDate = expiryDate;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdCard idCard = (IdCard) o;
        return Objects.equals(getId(), idCard.getId()) && Objects.equals(getUserId(), idCard.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId());
    }
}
