package org.meristem.oneapp.usersservice.repositories;


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

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET onboarding_completed = TRUE WHERE user_id = :userId ")
    void completeOnboarding(Long userId);

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
}
