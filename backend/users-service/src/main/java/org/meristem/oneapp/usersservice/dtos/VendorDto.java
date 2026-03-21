package org.meristem.oneapp.usersservice.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public record VendorDto(Integer failCount, LocalDateTime lastSuccessfulResponse) implements Serializable {
}
