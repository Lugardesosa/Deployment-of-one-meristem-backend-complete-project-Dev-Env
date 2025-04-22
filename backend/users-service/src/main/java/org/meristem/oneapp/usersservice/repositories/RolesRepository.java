package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.models.Roles;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.List;

public interface RolesRepository extends BaseRepository<Roles, Long> {

    @Query("SELECT r.name, r.id FROM roles r LEFT JOIN users_roles ur ON ur.roles_id = r.id WHERE ur.users_id = :id ")
    List<Roles> findAllByUsersId(Long id);

    @Query("SELECT id FROM roles WHERE name = :name")
    Long findIdByName(String name);
}
