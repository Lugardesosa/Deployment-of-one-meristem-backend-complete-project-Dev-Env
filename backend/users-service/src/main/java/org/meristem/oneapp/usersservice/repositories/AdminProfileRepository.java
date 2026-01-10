package org.meristem.oneapp.usersservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.models.AdminProfile;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AdminProfileRepository extends BaseRepository<AdminProfile, Long> {

    @Query("SELECT a.admin_id FROM admin_profile a")
    List<Long> findAllAdminIds();

    @Transactional
    @Query("UPDATE admin_profile SET investment_instrument_id = :subsidiaryId")
    int updateAdminProfile(@NotNull(message = "Cannot be null") Long subsidiaryId);
}
