package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotNull;

public record UpdateAdminRequest(@NotNull(message = "Cannot be null") Long adminId, @NotNull(message = "Cannot be null") Long roleId, @NotNull(message = "Cannot be null") Long subsidiaryId) {
}
