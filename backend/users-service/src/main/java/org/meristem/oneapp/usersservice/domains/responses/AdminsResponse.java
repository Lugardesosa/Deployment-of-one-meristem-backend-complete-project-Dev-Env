package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.List;

public record AdminsResponse(List<Admin> admins) {

    public record Admin(Long id, String firstName, String lastName, String email, String phoneNumber, Integer status,
                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d MMM yyyy", timezone = "UTC") LocalDateTime createdDate,
                        @Column("display_name") String role, @Column("code") String subsidiary, @Column("roleId") Long roleId, @Column("investment_instrument_id") Long subsidiaryId) {
    }
}
