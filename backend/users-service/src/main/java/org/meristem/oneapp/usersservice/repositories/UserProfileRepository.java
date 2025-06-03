package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.UserProfile;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional(readOnly = true)
public interface UserProfileRepository extends BaseRepository<UserProfile, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET avatar_url = :url WHERE user_id = :userId  ")
    int updateUsersAvatar(String url, Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET pin = :pin WHERE user_id = :userId ")
    void updateUsersPin(String pin, Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET gender = :gender, date_of_birth = :dob WHERE user_id = :userId ")
    void updateUsersDobAndGender(String gender, LocalDate dob, long userId);

    @Modifying
    @Transactional
    @Query("UPDATE user_profile SET onboarding_completed = TRUE WHERE user_id = :userId ")
    void completeOnboarding(Long userId);
}
