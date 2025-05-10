package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.Address;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AddressRepository extends BaseRepository<Address, Long> {
    boolean existsByUserId(Long userId);

    boolean existsByUserIdAndApproved(Long userId, Integer approved);

    boolean existsByUserIdAndProcessing(Long userId, Integer processing);

    Optional<Address> findByUserId(Long userId);

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
