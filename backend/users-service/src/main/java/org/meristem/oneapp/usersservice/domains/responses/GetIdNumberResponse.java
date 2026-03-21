package org.meristem.oneapp.usersservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;

public record GetIdNumberResponse(@Schema(example = "12345678901", description = "Customer's id number") String idNumber) {
}
