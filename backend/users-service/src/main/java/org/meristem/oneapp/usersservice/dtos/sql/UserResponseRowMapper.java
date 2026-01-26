package org.meristem.oneapp.usersservice.dtos.sql;

import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResponseRowMapper implements RowMapper<UsersResponse> {
    @Override
    public UsersResponse mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return AppUtil.buildUsersResponseMini(rs);
    }
}
