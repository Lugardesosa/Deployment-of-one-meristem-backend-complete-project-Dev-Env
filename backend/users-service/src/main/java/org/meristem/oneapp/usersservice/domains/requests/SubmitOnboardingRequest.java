package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.meristem.oneapp.usersservice.constants.AppConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SubmitOnboardingRequest(@Schema(example = "1", description = "Pass the requirementId that the client has completed and want to record to the db") @NotNull(message = "Cannot be null") Long requirementId,
                                      @Schema(example = "http://placeimg.com/640/480", description = "The url where the document linked to this onboarding flow was uploaded to") @URL(message = "provide a valid url", regexp = AppConstants.URL_REGEX_PATTERN) String documentUrl,
                                      @Schema(description = "Pass the user's address if submitting address verification") @Valid AddressRequest addressRequest,
                                      @Schema(description = "Pass the user's id request if submitting id verification") @Valid IdRequest idRequest,
                                      @Schema(description = "Pass the user's details from their nin") @Valid BvnUserRequest userBvnRequest) {
}

