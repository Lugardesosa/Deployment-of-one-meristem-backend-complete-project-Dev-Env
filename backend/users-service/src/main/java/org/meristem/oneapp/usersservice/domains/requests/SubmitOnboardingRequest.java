package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.meristem.oneapp.usersservice.constants.AppConstants;

// TODO: GET USER_ID FROM SECURITY CONTEXT
@JsonIgnoreProperties(ignoreUnknown = true)
public record SubmitOnboardingRequest(@NotNull(message = "Cannot be null") Long requirementId, @NotBlank(message = "cannot be null") @URL(message = "provide a valid url", regexp = AppConstants.URL_REGEX_PATTERN) String documentUrl,
                                      @NotNull(message = "Cannot be null") Long featureId, @NotNull(message = "Cannot be null") Long userId, @NotBlank(message = "cannot be null") String requirementName) {
}
