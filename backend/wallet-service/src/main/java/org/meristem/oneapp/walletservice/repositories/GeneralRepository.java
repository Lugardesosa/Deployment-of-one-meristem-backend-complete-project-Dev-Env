package org.meristem.oneapp.walletservice.repositories;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.walletservice.config.configProperties.WemaConfigProperties;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeneralRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final WemaConfigProperties wemaConfigProperties;

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

    public String generateWemaVirtualAccount() {
        Long nextVal = jdbcTemplate.queryForObject( "SELECT nextval('wema_virtual_account_seq')", Map.of(), Long.class);
        return wemaConfigProperties.accountPrefix() + String.format("%07d", nextVal);
    }
}
