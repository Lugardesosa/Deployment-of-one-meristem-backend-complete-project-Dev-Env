package org.meristem.oneapp.usersservice.dtos.sql;

import org.jspecify.annotations.Nullable;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import static java.util.Objects.nonNull;

public class UserInvestmentOptionsResultSetExtractor implements ResultSetExtractor<UsersResponse> {
    @Override
    public UsersResponse extractData(ResultSet rs) throws SQLException, DataAccessException {

        if (!rs.next()) {
            return null;
        }

        UsersResponse user = AppUtil.buildUsersResponse(rs);
        do {
            if (!user.userOptionResponses().containsKey(rs.getString("code"))) {

                user.userInstrumentResponses().add(UsersResponse.UserInstrumentResponse.builder()
                        .name(rs.getString("name")).id(rs.getLong("iiid"))
                        .code(rs.getString("code"))
                        .accessed(rs.getBoolean("accessed"))
                        .dataSharingAllowed(rs.getBoolean("data_sharing_allowed"))
                        .build());

                user.userOptionResponses().put(rs.getString("code"), new HashSet<>());
            }
            if (nonNull(rs.getString("o_name"))) {
                user.userOptionResponses().get(rs.getString("code")).add(
                        UsersResponse.UserOptionResponse.builder().accessed(rs.getBoolean("o_accessed"))
                                .name(rs.getString("o_name")).id(rs.getLong("o_iiid")).build()
                );
            }
        } while (rs.next());
        return user;
    }

}
