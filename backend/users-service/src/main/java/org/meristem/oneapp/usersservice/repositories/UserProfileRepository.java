package org.meristem.oneapp.usersservice.repositories;


import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.dtos.sql.SecUserDetails;
import org.meristem.oneapp.usersservice.dtos.sql.VerificationDetails;
import org.meristem.oneapp.usersservice.models.UserProfile;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserProfileRepository extends BaseRepository<UserProfile, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET image_key = :imageKey WHERE user_id = :userId  ")
    int updateUsersImage(String imageKey, Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET pin = :pin WHERE user_id = :userId ")
    void updateUsersPin(String pin, Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET gender = :gender, date_of_birth = :dob, country_of_origin = :countryOfOrigin WHERE user_id = :userId ")
    void updateUsersDobAndGender(String gender, LocalDate dob, String countryOfOrigin, long userId);

//    @Modifying
//    @Transactional
//    @Query("UPDATE user_profile SET onboarding_completed = TRUE WHERE user_id = :userId ")
//    void completeOnboarding(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET onboarding_completed = FALSE WHERE user_id = :userId ")
    void resetOnboarding(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET date_of_birth = :dob WHERE user_id = :userId ")
    int updateDob(long userId, LocalDate dob);


    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET gender = :gender WHERE user_id = :userId ")
    int updateGender(long userId, String gender);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET state_of_origin = :name WHERE user_id = :userId ")
    int updateState(long userId, String name);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET country_of_origin = :name WHERE user_id = :userId ")
    int updateCountry(long userId, String name);

    Optional<UserProfile> findByUserId(Long userId);

    boolean existsByReferralCode(String referralCode);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET email_verified = :value WHERE user_id = :userId ")
    void updateEmailVerified(Long userId, boolean value);

    @Query("SELECT email_verified FROM user_profile WHERE user_id = :userId")
    boolean findEmailVerifiedByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET cscs_number = :cscs WHERE user_id = :userId ")
    void updateUsersCscs(Long userId, String cscs);

    @Query("SELECT data_sharing FROM user_profile up WHERE up.user_id = :id ")
    Boolean findDataSharingByUserId(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET phone_number_verified = :value WHERE user_id = :userId ")
    void updatePhoneNumberVerified(Long userId, boolean value);

    @Query("SELECT bvn_verified FROM user_profile up LEFT JOIN id_card ic ON up.user_id = ic.user_id WHERE ic.id_value_hashed = :key")
    boolean bvnVerified(String key);

    @Query("SELECT bvn_verified FROM user_profile up WHERE user_id = :userId")
    boolean bvnVerified(Long userId);

    @Query("SELECT nin_verified FROM user_profile up WHERE user_id = :userId")
    boolean ninVerified(Long userId);

    @Query("SELECT address_verified FROM user_profile up WHERE user_id = :userId")
    boolean addressVerified(Long userId);

    @Query("SELECT bvn_verified, nin_verified, address_verified FROM user_profile up WHERE user_id = :userId")
    VerificationDetails verificationDetails(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET bvn_verified = :value WHERE user_id = :userId ")
    void updateBvnVerified(Long userId, boolean value);

    @Query("SELECT u.phone_number, u.id FROM users u LEFT JOIN id_card idc ON idc.user_id = u.id WHERE idc.id_value_hashed = :hashedValued")
    UsersResponse.UsersDetails findUserPhoneNumberByHashedBvn(String hashedValued);

    @Query("SELECT up.bvn_verified, up.user_id FROM joint_account ja_me JOIN joint_account ja_other ON ja_other.customer_id = ja_me.customer_id AND ja_other.user_id <> ja_me.user_id AND ja_other.role = '2' JOIN user_profile up ON up.user_id = ja_other.user_id WHERE ja_me.user_id = :userId")
    SecUserDetails getSecUserDetails(Long userId);


    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET nin_verified = :value WHERE user_id = :userId ")
    void updateNinVerified(Long userId, boolean value);


    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET address_verified = :value WHERE user_id = :userId ")
    void updateAddressVerified(Long userId, boolean value);
}
