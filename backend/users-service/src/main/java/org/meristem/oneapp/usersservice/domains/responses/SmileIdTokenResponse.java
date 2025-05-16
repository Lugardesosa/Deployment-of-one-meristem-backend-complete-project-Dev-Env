package org.meristem.oneapp.usersservice.domains.responses;


public record SmileIdTokenResponse(String token, String jobId, String signature) {
}
