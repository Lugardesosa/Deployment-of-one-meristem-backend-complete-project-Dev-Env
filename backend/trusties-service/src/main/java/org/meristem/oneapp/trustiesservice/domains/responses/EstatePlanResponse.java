package org.meristem.oneapp.trustiesservice.domains.responses;


import lombok.*;

@Builder
public record EstatePlanResponse(String message, Boolean status, Long id) {

}
