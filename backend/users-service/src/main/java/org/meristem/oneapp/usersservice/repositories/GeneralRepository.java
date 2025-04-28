package org.meristem.oneapp.usersservice.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class GeneralRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public int update(String tableName, Map<String, Object> params, Map<String, Object> condition) {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE ");
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        return jdbcTemplate.update(sql.toString(), params);
    }
}
