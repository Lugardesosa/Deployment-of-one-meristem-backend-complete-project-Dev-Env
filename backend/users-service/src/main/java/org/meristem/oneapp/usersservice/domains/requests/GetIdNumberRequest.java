package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.Pattern;

public record GetIdNumberRequest(@Pattern(regexp = "^BVN|NIN$", message = "Pass a valid id type (BVN or NIN)") String idType) {
}
