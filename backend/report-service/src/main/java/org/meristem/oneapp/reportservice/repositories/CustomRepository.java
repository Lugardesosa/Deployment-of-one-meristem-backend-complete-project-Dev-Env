package org.meristem.oneapp.reportservice.repositories;


import org.meristem.oneapp.reportservice.domains.responses.ActivityLogResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CustomRepository extends GeneralRepository {

    public CustomRepository(NamedParameterJdbcTemplate jdbcTemplate, JdbcAggregateTemplate jdbcAggregateTemplate) {
        super(jdbcTemplate, jdbcAggregateTemplate);
    }


    public <T, R> List<ActivityLogResponse> findAllActivityLogs(Map<String, Object> conditions, ResultSetExtractor<List<ActivityLogResponse>> resultSetExtractor, PageRequest pageRequest) {
        StringBuilder sql = new StringBuilder("SELECT a.*, alm.map_key, alm.map_value FROM activity_log a LEFT JOIN activity_log_metadata alm ON a.id = alm.activity_log_id WHERE ");

        sql = new StringBuilder(getAllString(conditions, sql)).append(" LIMIT :limit").append(" OFFSET :offset");
        conditions.put("limit", pageRequest.getPageSize());
        conditions.put("offset", pageRequest.getOffset() * pageRequest.getPageSize());

        return jdbcTemplate.query (sql.toString(), conditions, resultSetExtractor);


//        List<R> page = jdbcTemplate.query(sql.toString(), conditions, rowMapper);
//        return new PageImpl<>(page, pageRequest, page.size());
    }
}
