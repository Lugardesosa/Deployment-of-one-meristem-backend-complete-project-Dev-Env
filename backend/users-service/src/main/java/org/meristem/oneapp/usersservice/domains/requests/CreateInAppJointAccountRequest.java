package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.domains.enums.MandateType;

public record CreateInAppJointAccountRequest(@NotNull(message = "Cannot be null") MandateType mandateType,
                                             @NotNull(message = "Cannot be null") @Valid CreateUserRequest secondary) {
}
