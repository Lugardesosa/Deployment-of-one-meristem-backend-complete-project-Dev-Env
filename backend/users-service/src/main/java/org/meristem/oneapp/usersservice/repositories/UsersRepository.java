package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.usersservice.domains.annotations.UsersQueryModifier;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.dtos.sql.UserResponseResultSetExtractor;
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
    @Cacheable(value = "users", key = "#a0", unless = "#result == null")
    @Query(value = "SELECT u.*, up.image_key, up.pin, up.gender, up.date_of_birth, up.referral_code, up.onboarding_completed, up.biometric_enabled, ii.code, ii.id AS iiid, ii.name, ia.accessed, " +
            "io.id AS o_iiid, io.name AS o_name, ioa.accessed AS o_accessed FROM users u LEFT JOIN user_profile up ON u.id = up.user_id " +
            "LEFT JOIN instrument_accessed ia ON ia.user_id = u.id LEFT JOIN investment_instruments ii ON ii.id = ia.instrument_id " +
            " LEFT JOIN investment_options io ON io.investment_id = ii.id LEFT JOIN investment_options_accessed ioa ON ioa.option_id = io.id " +
            "WHERE u.email = :email AND u.password IS NOT NULL ", resultSetExtractorClass = UserResponseResultSetExtractor.class)
    Optional<UsersResponse> findUserDetailsByEmail(String email);

    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

    @UsersQueryModifier
    @Query("UPDATE users SET password = :password, password_attempt = 0, status = 1 WHERE email = :userId ")
    int updateUsersPassword(String userId, String password);

    @Query("SELECT password, email FROM users u WHERE u.email = :recipient OR u.phone_number = :recipient ")
    UsersResponse findUserByEmailOrPhoneNumber(String recipient);

    @Query("SELECT password FROM users u WHERE u.email = :recipient OR u.phone_number = :recipient ")
    String findPasswordByEmailOrPhoneNumber(String recipient);

    @Query("SELECT pin FROM user_profile u WHERE u.user_id = :userId ")
    String findPinByEmailOrPhoneNumber(Long userId);

    @Query("SELECT email FROM users u WHERE u.id = :id ")
    String findEmailById(Long id);

    @UsersQueryModifier
    @Query("UPDATE users SET phone_number = :phoneNumber WHERE email = :email ")
    void updateUsersPhoneNumber(String email, String phoneNumber);

    @UsersQueryModifier
    @Query("UPDATE users SET password_attempt = :attempt WHERE email = :email ")
    void updatePasswordAttempt(String email, Integer attempt);

    @UsersQueryModifier
    @Query("UPDATE users SET status = :userStatus WHERE email = :email ")
    void updateStatus(String email, Integer userStatus);

    Optional<Users> findOneByEmail(String email);

    @UsersQueryModifier
    @Query("UPDATE users SET status = :status WHERE id = :id ")
    int updateUsersStatus(long id, Integer status);

    Long findIdByEmail(String email);

    @Query("SELECT u.id, u.first_name, u.last_name, u.phone_number, u.email, a.house_address, i.id_value, up.date_of_birth FROM users u " +
            "LEFT JOIN address a ON a.user_id = u.id LEFT JOIN id_card i ON i.user_id = u.id LEFT JOIN user_profile up ON up.user_id = u.id " +
            " WHERE u.email = :userId ")
    KycCompletedDto getUserKyc(String userId);

    Optional<Users> findOneByEmailAndPasswordIsNull(String email);

    @Query("SELECT u.* FROM users u " +
            "LEFT JOIN user_profile up ON up.user_id = u.id " +
            " WHERE u.email = :email AND up.email_verified = FALSE ")
    Optional<Users> findOneByEmailAndEmailVerifiedIsNull(String email);

    @UsersQueryModifier
    @Query("UPDATE users SET email = :email WHERE id = :id ")
    int updateEmail(Long userId, String email);
}
