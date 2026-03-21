package org.meristem.oneapp.usersservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Represents an OTP (One-Time Password) verification entity.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpVerificationDto implements Serializable {

    private Integer code;

    private String userId;

    @Builder.Default
    private Boolean verified = false;

    private LocalDateTime expiresAt;

    private Integer otpType;
}
