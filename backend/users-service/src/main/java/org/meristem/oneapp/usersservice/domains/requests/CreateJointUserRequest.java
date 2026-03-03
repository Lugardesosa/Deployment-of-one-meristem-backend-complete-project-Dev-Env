package org.meristem.oneapp.usersservice.domains.requests;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.MandateType;
import org.meristem.oneapp.usersservice.domains.enums.OperationType;

@Builder
public record CreateJointUserRequest(@NotBlank(message = "Cannot be blank") String accountName,
                                     @NotNull(message = "Cannot be null") MandateType mandateType,
                                     @NotNull(message = "Cannot be null") OperationType operationType,
                                     @NotNull(message = "Cannot be null") @Valid CreateUserRequest primary,
                                     @NotNull(message = "Cannot be null") @Valid CreateUserRequest secondary) {
}
