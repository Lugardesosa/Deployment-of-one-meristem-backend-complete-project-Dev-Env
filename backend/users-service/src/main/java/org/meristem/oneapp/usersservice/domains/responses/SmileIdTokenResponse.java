package org.meristem.oneapp.usersservice.domains.responses;


import lombok.Builder;

@Builder
public record SmileIdTokenResponse(String link, String jobId, String signature, String timestamp) {

    public SmileIdTokenResponse(String link, String jobId) {
        this(link, jobId, null, null);
    }
}
