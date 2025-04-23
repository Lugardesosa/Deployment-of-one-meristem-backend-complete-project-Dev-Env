package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.models.Users;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface UsersRepository extends BaseRepository<Users, Long> {

    @NonNull
    @Override
    @CacheEvict(cacheNames = "users", key = "#result.email")
    <S extends Users> S save(@NonNull S entity);

    @Cacheable(value = "users", key = "#a0")
    @Query("SELECT * FROM users u LEFT JOIN user_profile up ON u.id = up.user_id WHERE u.email = :email ")
    UsersResponse findByEmail(String email);

    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE users SET password = :password WHERE email = :userId OR phone_number = :userId ")
    int updateUsersPassword(String password, String userId);

    @Modifying
    @Transactional
    @Query("UPDATE users SET onboarding_completed = TRUE WHERE id = :userId ")
    void completeOnboarding(Long userId);
}
