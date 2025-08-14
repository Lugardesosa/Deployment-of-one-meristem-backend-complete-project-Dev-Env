package org.meristem.oneapp.coreservice.domains.responses;


import lombok.*;

@Builder
public record EstatePlanResponse(String message, Boolean status, Long id) {

}
