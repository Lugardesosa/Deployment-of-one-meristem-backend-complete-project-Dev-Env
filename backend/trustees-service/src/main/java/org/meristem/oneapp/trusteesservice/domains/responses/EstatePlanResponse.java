package org.meristem.oneapp.trusteesservice.domains.responses;


import lombok.Builder;

@Builder
public record EstatePlanResponse(String message, Boolean status, Long id) {

}
