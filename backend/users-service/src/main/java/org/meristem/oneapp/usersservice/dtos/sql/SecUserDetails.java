package org.meristem.oneapp.usersservice.dtos.sql;

import lombok.Builder;

@Builder
public record SecUserDetails(Long userId, boolean bvnVerified) {
}
