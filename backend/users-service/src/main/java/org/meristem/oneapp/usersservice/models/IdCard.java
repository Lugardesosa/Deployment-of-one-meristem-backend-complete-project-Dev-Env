package org.meristem.oneapp.usersservice.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
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

    // ENCRYPTED
    @Column("id_value")
    @NotBlank(message = "cannot be null")
    String idValue;

    @Column("id_value_hashed")
    @NotBlank(message = "cannot be null")
    String idValueHashed;

    @Column("issued_date")
    LocalDate issuedDate;

    @Column("expiry_date")
    LocalDate expiryDate;

    @Column("user_id")
    @NotNull(message = "User id cannot be null")
    private Long userId;

    private Integer accountType;

    @Builder
    public IdCard(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                  String idCardType, String idValue, LocalDate issuedDate, LocalDate expiryDate, Long userId, String idValueHashed, Integer accountType) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.idCardType = idCardType;
        this.idValue = idValue;
        this.issuedDate = issuedDate;
        this.expiryDate = expiryDate;
        this.userId = userId;
        this.idValueHashed = idValueHashed;
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdCard idCard = (IdCard) o;
        return Objects.equals(getId(), idCard.getId()) && Objects.equals(getIdValueHashed(), idCard.getIdValueHashed()) && Objects.equals(getAccountType(), idCard.getAccountType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIdValueHashed(), getAccountType());
    }
}
