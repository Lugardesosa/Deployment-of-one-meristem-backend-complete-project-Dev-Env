package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record SecondaryUserRegResponse(String bvn) {
}
