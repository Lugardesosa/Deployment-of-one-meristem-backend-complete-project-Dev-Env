package org.meristem.oneapp.usersservice.domains.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserInstrumentRequest(@Schema(example = "1", description = "The instrument id") @NotNull(message = "Cannot be null") Long instrumentId) {
}
