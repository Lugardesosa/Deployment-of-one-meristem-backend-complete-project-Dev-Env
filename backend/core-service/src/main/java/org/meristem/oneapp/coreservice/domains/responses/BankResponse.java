package org.meristem.oneapp.coreservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record BankResponse(@Schema(description = "The name of the bank", example = "Access Bank Plc") String name,
                           @Schema(description = "The id of the bank", example = "1") Long id) {
}
