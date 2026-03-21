package org.meristem.oneapp.usersservice.integrations.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record DojahBvnVerificationRequest(@JsonProperty("selfie_image") String selfieImage, String bvn) {
}
