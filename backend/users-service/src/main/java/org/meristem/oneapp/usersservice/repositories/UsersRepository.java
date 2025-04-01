package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.meristem.oneapp.usersservice.domains.responses.UserResponse;
import org.meristem.oneapp.usersservice.models.Users;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "users", key = "#a0")
    @Query("SELECT * FROM users u LEFT JOIN user_profile up ON u.id = up.user_id WHERE u.email = :email ")
    UserResponse findByEmail(String email);

    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);
}
