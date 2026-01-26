package org.meristem.oneapp.usersservice.domains.responses;

import java.util.List;

public record PermissionsResponse(List<Permission> permissions) {


    public record Permission(Long id, String name, String description) {
    }
}
