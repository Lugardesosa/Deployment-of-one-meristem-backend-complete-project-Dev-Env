package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.meristem.oneapp.usersservice.integrations.requests.SmileIdSmileLinkRequest;

import java.util.List;

public record SmileIdIdTypeRequest(@NotEmpty(message = "Cannot be empty") List<SmileIdSmileLinkRequest.@Valid IdType> idTypes) {
}
