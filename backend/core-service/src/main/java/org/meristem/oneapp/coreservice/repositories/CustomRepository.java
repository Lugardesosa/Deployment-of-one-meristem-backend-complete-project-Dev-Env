package org.meristem.oneapp.coreservice.repositories;


import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.coreservice.domains.requests.UpdateSelectionRequest;
import org.meristem.oneapp.coreservice.exception.exceptions.BadRequestException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class CustomRepository extends GeneralRepository {

    public CustomRepository(NamedParameterJdbcTemplate jdbcTemplate, JdbcAggregateTemplate jdbcAggregateTemplate) {
        super(jdbcTemplate, jdbcAggregateTemplate);
    }

    public int deleteSelections(List<UpdateSelectionRequest.Items> itemsToDelete) {

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (itemsToDelete.isEmpty()) {
            throw new BadRequestException("Condition cannot be empty");
        }

        StringBuilder sql = new StringBuilder("DELETE FROM selections WHERE ");

        for (UpdateSelectionRequest.Items item : itemsToDelete) {
            sql.append(" ( form_id").append(" = :").append("form_id").append(item.formId()).append(" AND ")
                    .append(" selection_value = :").append("selection_value").append(item.selectionValue().replaceAll("\\s", "")).append(" ) OR ");
            params.addValue("form_id" + item.formId(), item.formId());
            params.addValue("selection_value" + item.selectionValue().replaceAll("\\s", ""), item.selectionValue());
        }

        return jdbcTemplate.update(sql.substring(0, sql.length() - 3), params);
    }

    public List<?> findPlans(Class<?> clazz, Map<String, Object> filters, ResultSetExtractor<List<?>> resultSetExtractor) {

        String sql = "SELECT p.*, pa.asset_id AS assetId, pa.asset_type, pb.beneficiary_id, pb.percentage FROM " + getTableName(clazz) +
                " p " +
                " LEFT JOIN plan_assets pa ON p.id = pa.plan_id " +
                " LEFT JOIN plan_beneficiaries pb ON p.id = pb.plan_id " +
                " WHERE p.owner_id = :owner_id " +
                (filters.containsKey("id") ? " AND p.id = :id " : "");
        return jdbcTemplate.query(sql, filters, resultSetExtractor);
    }
}
