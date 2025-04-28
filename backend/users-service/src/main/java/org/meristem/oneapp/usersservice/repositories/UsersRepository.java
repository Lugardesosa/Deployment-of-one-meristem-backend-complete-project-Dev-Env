package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.models.Users;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    @Query("SELECT u.*, up.avatar_url, up.pin, up.gender, up.date_of_birth, up.referral_code, up.onboarding_completed FROM users u LEFT JOIN user_profile up ON u.id = up.user_id WHERE u.email = :email AND u.status = :status ")
    UsersResponse findUserDetailsByEmailAndStatus(String email, Integer status);

    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

    @CacheEvict(cacheNames = "users", key = "#a1")
    @Modifying
    @Transactional
    @Query("UPDATE users SET password = :password WHERE email = :userId OR phone_number = :userId ")
    int updateUsersPassword(String password, String userId);

    @Query("SELECT password, email FROM users u WHERE u.email = :recipient OR u.phone_number = :recipient ")
    UsersResponse findUserByEmailOrPhoneNumber(String recipient);

    @Query("SELECT password FROM users u WHERE u.email = :recipient OR u.phone_number = :recipient ")
    String findPasswordByEmailOrPhoneNumber(String recipient);


    @Query("SELECT pin FROM user_profile u WHERE u.user_id = :userId ")
    String findPinByEmailOrPhoneNumber(Long userId);

    @CacheEvict(cacheNames = "users")
    Optional<Users> findByEmailAndStatus(String email, Integer status);

    @CacheEvict(cacheNames = "users", key = "#a1")
    @Modifying
    @Transactional
    @Query("UPDATE users SET phone_number = :phoneNumber WHERE email = :email ")
    void updateUsersPhoneNumber(String phoneNumber, String email);
}
