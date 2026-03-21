package org.meristem.oneapp.usersservice.dtos.sql;

import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;

public class UserInvestmentOptionsResultSetExtractor implements ResultSetExtractor<Map<String, Set<UsersResponse.UserOptionResponse>>> {
    @Override
    public Map<String, Set<UsersResponse.UserOptionResponse>> extractData(ResultSet rs) throws SQLException, DataAccessException {

        if (!rs.next()) {
            return null;
        }
        Map<String, Set<UsersResponse.UserOptionResponse>> response = new HashMap<>();

        do {
            if (!response.containsKey(rs.getString("code"))) {

                response.put(rs.getString("code"), new HashSet<>());
            }
            if (nonNull(rs.getString("o_name"))) {
                response.get(rs.getString("code")).add(
                        UsersResponse.UserOptionResponse.builder()
                                .accessed(rs.getBoolean("o_accessed"))
                                .name(rs.getString("o_name"))
                                .id(rs.getLong("o_iiid"))
                                .build()
                );
            }
        } while (rs.next());
        return response;
    }

}
