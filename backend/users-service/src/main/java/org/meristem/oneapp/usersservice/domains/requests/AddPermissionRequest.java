package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotBlank;

public record AddPermissionRequest(@NotBlank(message = "Cannot be null") String permissionName, Long roleId) {
}
