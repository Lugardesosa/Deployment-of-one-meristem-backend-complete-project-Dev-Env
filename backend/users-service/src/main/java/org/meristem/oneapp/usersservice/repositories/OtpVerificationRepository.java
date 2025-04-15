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

    @Transactional
    @Modifying
    @Query("UPDATE otp_verification SET expires_at = :time WHERE id = (SELECT id FROM otp_verification " +
            "WHERE (user_id = :email OR user_id = :phone) AND otp_type = :otpType ORDER BY expires_at DESC LIMIT 1)")
    void expireTimeByCodeAndEmailOrPhone(LocalDateTime time, String email, String phone, Integer otpType);

    Optional<OtpVerification> findByOtpTypeAndCodeAndUserId(Integer otpType, Integer code, String userId);

//    @Transactional
//    @Modifying
//    @Query("UPDATE otp_verification SET verified = TRUE WHERE otp_type = :otpType AND (user_id = :userId OR user_id = :phone)")
//    int markOtpAsUsed(Integer otpType, String userId, String phone);

    @Query("SELECT CASE WHEN COUNT(id) > 0 THEN TRUE ELSE FALSE END FROM otp_verification o WHERE o.otp_type = :otpType AND (o.user_id = :email OR o.user_id = :phone) AND (o.verified = :verified) AND (o.expires_at > :expiresAtAfter) ")
    boolean existsByOtpTypeAndUserIdAndVerifiedAndExpiresAtAfter(Integer otpType, String email, String phone, Boolean verified, LocalDateTime expiresAtAfter);

    @Query("SELECT * FROM otp_verification o WHERE o.otp_type = :otpType AND (o.user_id = :userId OR o.user_id = :phone) AND (o.verified = :verified)")
    Optional<OtpVerification> findByOtpTypeAndUserIdAndVerified(Integer otpType, String userId, String phone, Boolean verified);

//    @Query("SELECT CASE WHEN COUNT(id) > 0 THEN TRUE ELSE FALSE END FROM otp_verification o WHERE o.otp_type = :otpType AND (o.user_id = :userId) AND (o.verified = :verified) AND (o.expires_at > :expiresAtAfter) ")
//    boolean existsByOtpTypeAndUserIdAndVerifiedAndExpiresAtAfter(Integer otpType, String userId, Boolean verified, LocalDateTime expiresAtAfter);
}
