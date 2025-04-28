package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.Permissions;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.List;

public interface PermissionsRepository extends BaseRepository<Permissions, Long> {

    @Query("SELECT p.name FROM permissions p LEFT JOIN roles_permissions rp ON rp.permissions_id = p.id WHERE rp.roles_id IN (:ids) ")
    List<String> findAllByRolesIds(List<Long> ids);
}
