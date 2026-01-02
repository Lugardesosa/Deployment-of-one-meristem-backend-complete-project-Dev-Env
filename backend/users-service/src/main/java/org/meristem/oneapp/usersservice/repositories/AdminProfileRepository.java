package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.AdminProfile;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.List;

public interface AdminProfileRepository extends BaseRepository<AdminProfile, Long> {

    @Query("SELECT a.admin_id FROM admin_profile a")
    List<Long> findAllAdminIds();
}
