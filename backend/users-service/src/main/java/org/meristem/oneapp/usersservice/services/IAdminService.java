package org.meristem.oneapp.usersservice.services;

import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;

/**
 * Interface for managing administrative operations.
 * Provides functionality for creating admin users, updating user details, and managing roles and permissions.
 *
 * @author Kingsley
 */
public interface IAdminService {

    /**
     * Updates the next-of-kin details for a user.
     *
     * @param request the request containing next-of-kin details
     * @return a {@link NextOfKinResponse} containing the updated next-of-kin details
     */
    NextOfKinResponse updateNextOfKin(CreateNextOfKinRequest request);

    /**
     * Updates the date of birth for a user.
     *
     * @param request the request containing user ID and date of birth
     * @return a {@link DobResponse} containing the update status
     */
    DobResponse updateDob(DobRequest request);

    /**
     * Updates the gender for a user.
     *
     * @param request the request containing user ID and gender
     * @return a {@link GenderResponse} containing the update status
     */
    GenderResponse updateGender(GenderRequest request);

    /**
     * Creates a new admin user with a randomly generated password.
     *
     * @param request the request containing admin user details
     * @return a {@link UsersResponse} containing the created admin user's details
     */
    UsersResponse create(CreateAdminRequest request);

    /**
     * Enables or disables admin user based on request.
     *
     * @param request the request containing user ID and enable flag
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse enable(EnableAdminRequest request);

    /**
     * Assigns a role to an admin user.
     *
     * @param request the request containing user ID and role ID
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse assignRole(AssignAdminRoleRequest request);

    /**
     * Assigns a permission to an admin user.
     *
     * @param request the request containing user ID and permission ID
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse assignPermission(AssignAdminPermissionRequest request);

    /**
     * Adds a new role to the system.
     *
     * @param request the request containing role name
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse addRole(AddRoleRequest request);

    /**
     * Adds a new permission to the system.
     *
     * @param request the request containing permission name
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse addPermission(AddPermissionRequest request);

    /**
     * Adds a permission to a role.
     *
     * @param request the request containing role ID and permission ID
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse addPermissionToRole(AddPermissionToRoleRequest request);

    /**
     * Retrieves roles for a user or all roles.
     *
     * @param userId the user ID or null for all roles
     * @return a {@link RolesResponse} containing the roles
     */
    RolesResponse getRoles(Long userId);

    /**
     * Retrieves permissions for a user, role, or all permissions.
     *
     * @param userId the user ID or null
     * @param roleId the role ID or null
     * @return a {@link PermissionsResponse} containing the permissions
     */
    PermissionsResponse getPermissions(Long userId, Long roleId);

    /**
     * Retrieves admins by ID or all admins.
     *
     * @param adminId the admin ID or null for all admins
     * @return an {@link AdminsResponse} containing the admins
     */
    AdminsResponse getAdmins(Long adminId);

    /**
     * Updates an admin's profile.
     *
     * @param request the request containing admin ID, subsidiary ID, and role ID
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse updateAdmin(UpdateAdminRequest request);
}
