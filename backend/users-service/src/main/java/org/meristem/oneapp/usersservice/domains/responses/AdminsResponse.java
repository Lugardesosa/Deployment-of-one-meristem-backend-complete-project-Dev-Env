package org.meristem.oneapp.usersservice.domains.responses;

import java.util.List;

public record AdminsResponse(List<Admin> admins) {

    public record Admin(Long id, String firstName, String lastName, String email, String phoneNumber) {
    }
}
