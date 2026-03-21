package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotNull;

public record AssignAdminPermissionRequest(@NotNull(message = "Cannot be null") Long userId, @NotNull(message = "Cannot be null") Long permissionId) {
}
