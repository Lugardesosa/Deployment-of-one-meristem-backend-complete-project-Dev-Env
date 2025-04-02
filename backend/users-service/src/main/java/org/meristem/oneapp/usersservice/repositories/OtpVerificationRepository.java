package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.OtpVerification;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional(readOnly = true)
public interface OtpVerificationRepository extends BaseRepository<OtpVerification, Long> {


    @Transactional
    @Modifying
    @Query("UPDATE otp_verification SET expires_at = :time WHERE id = (SELECT id FROM otp_verification " +
            "WHERE user_id = :userId AND otp_type = :otpType ORDER BY expires_at DESC LIMIT 1)")
    void expireTimeByCode(LocalDateTime time, String userId, Integer otpType);

    Optional<OtpVerification> findByOtpTypeAndCodeAndUserId(Integer otpType, Integer code, String userId);

    OtpVerification findByOtpTypeAndUserId(Integer otpType, String userId);

    Optional<OtpVerification> findByOtpTypeAndUserIdAndVerified(Integer otpType, String userId, Boolean verified);
}
