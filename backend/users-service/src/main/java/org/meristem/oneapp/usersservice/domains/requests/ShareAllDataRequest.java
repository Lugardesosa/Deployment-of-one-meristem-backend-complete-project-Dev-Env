package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import org.meristem.oneapp.usersservice.validations.constraints.Email;

public record ShareAllDataRequest(@Email(message = "enter a valid email address") String userEmail) {
}
