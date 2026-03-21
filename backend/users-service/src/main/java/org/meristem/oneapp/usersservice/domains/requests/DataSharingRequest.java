package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotNull;

public record DataSharingRequest(boolean dataSharing, @NotNull(message = "Cannot be null") Long instrumentId) {
}
