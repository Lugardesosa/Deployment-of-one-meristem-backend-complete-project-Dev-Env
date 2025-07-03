package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SmileIdIdTypeRequest(@NotEmpty(message = "Cannot be empty") List<@Valid IdTypesRequest> smileRequest, @NotBlank(message = "Cannot be empty") String idNumber) {
}
