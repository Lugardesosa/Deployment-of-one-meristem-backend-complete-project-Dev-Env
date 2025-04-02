package org.meristem.oneapp.usersservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;


@NoArgsConstructor
@Setter
@Getter
@Table("otp_verification")
public class OtpVerification extends BaseModel<String> {

    @Column("code")
    private Integer code;

    @Column("user_id")
    private String userId;

    @Column("verified")
    private Boolean verified;

    @Column("expires_at")
    private LocalDateTime expiresAt;

    @Column("otp_type")
    private Integer otpType;

    @Builder
    public OtpVerification(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy,
                           Integer version, LocalDateTime expiresAt, String userId, Integer code, Integer otpType) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.expiresAt = expiresAt;
        this.verified = false;
        this.userId = userId;
        this.code = code;
        this.otpType = otpType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OtpVerification that = (OtpVerification) o;
        return Objects.equals(getCode(), that.getCode()) && Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getOtpType(), that.getOtpType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getUserId(), getOtpType());
    }
}
