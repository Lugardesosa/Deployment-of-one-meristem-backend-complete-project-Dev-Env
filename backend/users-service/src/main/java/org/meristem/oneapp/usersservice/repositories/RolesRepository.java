package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.RolesResponse;
import org.meristem.oneapp.usersservice.models.Roles;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RolesRepository extends BaseRepository<Roles, Long> {

    @Query("SELECT r.name, r.id FROM roles r LEFT JOIN users_roles ur ON ur.roles_id = r.id WHERE ur.users_id = :id ")
    List<Roles> findAllByUsersId(Long id);

    @Query("SELECT id FROM roles WHERE name = :name")
    Long findIdByName(String name);

    @Modifying
    @Query("INSERT INTO users_roles(roles_id, users_id) VALUES ( :roleId, :userId ) ")
    int updateUserRole(Long userId, Long roleId);


    @Query("SELECT r.name FROM roles  ")
    List<String> findAllNames();

    @Query("SELECT r.id, r.name FROM roles r ")
    List<RolesResponse.Role> findAllRoles();

    @Query("SELECT r.name, r.id FROM roles r LEFT JOIN users_roles ur ON ur.roles_id = r.id WHERE ur.users_id = :userId ")
    List<RolesResponse.Role> findAllRoles(Long userId);

    @Query("SELECT r.id FROM roles r LEFT JOIN users_roles ur ON ur.roles_id = r.id WHERE ur.users_id = :userId ")
    List<Long> findAllRolesId(Long userId);

    @Query("SELECT r.name FROM roles r LEFT JOIN users_roles ur ON ur.roles_id = r.id WHERE ur.users_id = :userId ")
    List<String> findAllNamesByUserId(Long userId);
}
