package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotNull;

public record AddPermissionToRoleRequest(@NotNull(message = "Cannot be null") Long roleId, @NotNull(message = "Cannot be null") Long permissionId) {
}
