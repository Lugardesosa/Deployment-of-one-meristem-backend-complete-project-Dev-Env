package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.models.Address;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends BaseRepository<Address, Long> {
    boolean existsByUserId(Long userId);

    boolean existsByUserIdAndApproved(Long userId, Integer approved);

    boolean existsByUserIdAndProcessing(@NotNull(message = "Cannot be null") Long userId, @NotBlank(message = "Cannot be null") Integer processing);

    Optional<Address> findByUserId(@NotNull(message = "Cannot be null") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE address SET approved = :approved, processing = :processing WHERE user_id = :userId ")
    void updateApprovedAndProcessing(Integer approved, Integer processing, Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE address SET approved = TRUE WHERE user_id = :userId")
    void approveAddress(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE address SET processing = TRUE WHERE user_id = :userId")
    int processAddress(Long userId);
}
