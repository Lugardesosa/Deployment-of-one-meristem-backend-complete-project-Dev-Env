package org.meristem.oneapp.usersservice.domains.responses;

import java.util.List;

public record RolesResponse(List<Role> roles) {

    public record Role(Long id, String name, String displayName) {
    }
}
