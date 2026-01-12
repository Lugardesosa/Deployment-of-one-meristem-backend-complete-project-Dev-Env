package org.meristem.oneapp.usersservice.repositories;

import org.meristem.oneapp.usersservice.domains.responses.PermissionsResponse;
import org.meristem.oneapp.usersservice.models.Permissions;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PermissionsRepository extends BaseRepository<Permissions, Long> {

    @Query("SELECT p.name FROM permissions p LEFT JOIN permissions_mapping rp ON rp.permissions_id = p.id WHERE rp.roles_id IN (:ids) ")
    List<String> findAllByRolesIds(List<Long> ids);

    @Query("SELECT p.code FROM permissions p LEFT JOIN permissions_mapping rp ON rp.permissions_id = p.id WHERE rp.roles_id IN (:ids) ")
    List<String> findAllCodesByRolesIds(List<Long> ids);

//    @Query("SELECT p.name FROM permissions p LEFT JOIN permissions_mapping rp ON rp.permissions_id = p.id WHERE rp.users_id = :userId")
//    List<String> findAllByUsersId(Long userId);

//    @Modifying
//    @Query("INSERT INTO permissions_mapping(permissions_id, users_id) VALUES (:permissionId, :userId) ")
//    int updateUserPermission(Long userId, Long permissionId);

    @Query("SELECT p.name FROM permissions p ")
    List<String> findAllNames();

    //CASE WHEN COUNT(id) > 0 THEN TRUE ELSE FALSE END FROM user_onboarding
    @Query("SELECT CASE WHEN COUNT(permissions_id) > 0 THEN TRUE ELSE FALSE END FROM permissions_mapping WHERE roles_id = :rolesId AND permissions_id = :permissionsId ")
    boolean existsByRolesIdAndPermissionsId(Long rolesId, Long permissionsId);

    @Transactional
    @Modifying
    @Query("INSERT INTO permissions_mapping(permissions_id, roles_id) VALUES (:permissionId, :rolesId) ")
    int updateRolePermission(Long rolesId, Long permissionId);

    @Query("SELECT p.id, p.name FROM permissions p ")
    List<PermissionsResponse.Permission> findAllPermissions();

//    @Query("SELECT p.id, p.name FROM permissions p LEFT JOIN permissions_mapping pm ON pm.permissions_id = p.id WHERE pm.users_id = :userId OR pm.roles_id IN (:roleIds) ")
//    List<PermissionsResponse.Permission> findAllPermissionsByUserIdAndRoleIds(Long userId, List<Long> roleIds);

    @Query("SELECT p.id, p.name FROM permissions p LEFT JOIN permissions_mapping pm ON pm.permissions_id = p.id WHERE pm.roles_id = :roleId ")
    List<PermissionsResponse.Permission> findAllPermissionsByRoles(Long roleId);

//    @Query("SELECT p.id, p.name FROM permissions p LEFT JOIN permissions_mapping pm ON pm.permissions_id = p.id WHERE pm.users_id = :userId OR pm.roles_id IN (:roleIds) ")
//    List<PermissionsResponse.Permission> findAllNamesByUserIdAndRoleIds(Long userId, List<Long> roleIds);

    Permissions findByNameStartsWithOrderByCodeDesc(String name);

    Permissions findTopByNameStartsWithOrderByCodeDesc(String startsWith);

    @Query("SELECT p.id, p.name FROM permissions p LEFT JOIN permissions_mapping rp ON rp.permissions_id = p.id WHERE rp.roles_id IN (:roleIds) ")
    List<PermissionsResponse.Permission> findAllPermissionsByRoleIds(List<Long> roleIds);
}
