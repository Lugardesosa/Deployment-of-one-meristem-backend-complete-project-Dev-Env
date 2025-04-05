package org.meristem.oneapp.usersservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * Represents an OTP (One-Time Password) verification entity.
 */
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

/**
 * Constructs a new OtpVerification instance.
 *
 * @param id                the ID of the OTP verification
 * @param createdDate       the date and time when the OTP verification was created
 * @param createdBy         the user who created the OTP verification
 * @param lastModifiedDate  the date and time when the OTP verification was last modified
 * @param lastModifiedBy    the user who last modified the OTP verification
 * @param version           the version of the OTP verification
 * @param expiresAt         the expiration date and time of the OTP
 * @param userId            the ID of the user associated with the OTP
 * @param code              the OTP code
 * @param otpType           the type of OTP
 */
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

    /**
     * Checks if this OTP verification is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OtpVerification that = (OtpVerification) o;
        return Objects.equals(getCode(), that.getCode()) && Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getOtpType(), that.getOtpType());
    }

    /**
     * Computes the hash code for this OTP verification.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getUserId(), getOtpType());
    }
}
