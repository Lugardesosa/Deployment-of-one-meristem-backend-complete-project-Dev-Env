package org.meristem.oneapp.coreservice.domains.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
@Data
@Schema(description = "Request object for estate plan")
public class EstatePlanRequest {
}
