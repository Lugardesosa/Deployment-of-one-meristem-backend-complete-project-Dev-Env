package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotBlank;

public record AddRoleRequest(@NotBlank(message = "Cannot be null") String roleName) {
}
