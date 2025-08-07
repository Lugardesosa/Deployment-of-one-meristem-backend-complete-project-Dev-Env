package org.meristem.oneapp.walletservice.repositories;


import org.meristem.oneapp.walletservice.config.configProperties.WemaConfigProperties;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CustomRepository extends GeneralRepository {

    private final WemaConfigProperties wemaConfigProperties;

    public CustomRepository(NamedParameterJdbcTemplate jdbcTemplate, JdbcAggregateTemplate jdbcAggregateTemplate,  WemaConfigProperties wemaConfigProperties) {
        super(jdbcTemplate, jdbcAggregateTemplate);
        this.wemaConfigProperties = wemaConfigProperties;
    }

    public String generateWemaVirtualAccount() {
        Long nextVal = jdbcTemplate.queryForObject( "SELECT nextval('wema_virtual_account_seq')", Map.of(), Long.class);
        return wemaConfigProperties.accountPrefix() + String.format("%07d", nextVal);
    }
}
