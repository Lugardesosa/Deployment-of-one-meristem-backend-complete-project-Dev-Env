package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.models.Users;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional(readOnly = true)
public interface UsersRepository extends BaseRepository<Users, Long> {

    @NonNull
    @Override
    @CacheEvict(cacheNames = "users", key = "#result.email")
    <S extends Users> S save(@NonNull S entity);

    @Modifying
    @Query("INSERT INTO users_roles(users_id, roles_id) VALUES (:users_id, :rolesId)")
    void saveRole(Long users_id, Long rolesId);

    // TODO: INCREASE up COLUMNS AS THE TABLE INCREASES
    @Cacheable(value = "users", key = "#a0")
    @Query("SELECT u.*, up.picture_url FROM users u LEFT JOIN user_profile up ON u.id = up.user_id WHERE u.email = :email ")
    UsersResponse findUserDetailsByEmail(String email);

    // TODO: INCREASE up COLUMNS AS THE TABLE INCREASES
    @Query("SELECT u.*, up.picture_url FROM users u LEFT JOIN user_profile up ON u.id = up.user_id WHERE u.email = :email AND u.status = :status ")
    Optional<UsersResponse> findOneAndProfileByEmailAndStatus(String email, Integer status);

    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE users SET password = :password WHERE email = :userId OR phone_number = :userId ")
    int updateUsersPassword(String password, String userId);

    @Modifying
    @Transactional
    @Query("UPDATE users SET onboarding_completed = TRUE WHERE id = :userId ")
    void completeOnboarding(Long userId);

    @Query("SELECT password FROM users u WHERE u.email = :recipient OR u.phone_number = :recipient ")
    String findPasswordByEmailOrPhoneNumber(String recipient);

    Optional<Users> findByEmailAndStatus(String email, Integer status);
}
