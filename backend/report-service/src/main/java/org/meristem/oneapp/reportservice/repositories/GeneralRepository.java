package org.meristem.oneapp.reportservice.repositories;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.reportservice.domains.enums.NullCheck;
import org.meristem.oneapp.reportservice.dtos.sql.Comparison;
import org.meristem.oneapp.reportservice.dtos.sql.LikePattern;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.relational.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class GeneralRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JdbcAggregateTemplate jdbcAggregateTemplate;

    public int update(String tableName, Map<String, Object> params, Map<String, Object> condition) {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE ");
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(" AND,");
        }
        sql.deleteCharAt(sql.length() - 1);
        return jdbcTemplate.update(sql.toString(), params);
    }



    /**
     * <p>Follow the instructions below</p>
     * <p>Example usage of Map<String, Object> to define dynamic query conditions.
     * Each entry in the map represents a condition on a database field.
     * The value’s type determines the type of condition to generate.</p>
     *
     * <p>1. Equality Condition:
     *    conditions.put("fieldName", value);
     *    // SQL: fieldName = :fieldName</p>
     *
     * <p>2. Range (BETWEEN) Condition:
     *    conditions.put("fieldName", Pair.of(start, end));
     *    // SQL: fieldName BETWEEN :fieldName_start AND :fieldName_end</p>
     *
     * <p>3. IN Condition:
     *    conditions.put("fieldName", List.of(val1, val2, val3));
     *    // SQL: fieldName IN (:fieldName)</p>
     *
     * <p>4. LIKE Condition:
     *    conditions.put("fieldName", new LikePattern("%" + value + "%"));
     *    // SQL: fieldName LIKE :fieldName</p>
     *
     * <p>5. Comparison Operators (>, <, >=, <=):
     *    conditions.put("fieldName", new Comparison(value, Operator.GREATER_THAN));
     *    conditions.put("fieldName", new Comparison(value, Operator.LESS_THAN_OR_EQUAL));
     *    // SQL: fieldName > :fieldName OR fieldName <= :fieldName</p>
     *
     * <p>6. Null Check:
     *    conditions.put("fieldName", NullCheck.IS_NULL);
     *    conditions.put("fieldName", NullCheck.IS_NOT_NULL);
     *    // SQL: fieldName IS NULL OR fieldName IS NOT NULL</p>
     *
     *
     */
    public <T> Page<T> findAllBy(Class<T> tableName, Map<String, Object> conditions, PageRequest pageable) {


        List<CriteriaDefinition> criteriaDefinitions = new ArrayList<>();

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {

            switch (entry.getValue()) {
                case Pair<?, ?> pair -> criteriaDefinitions.add(where(entry.getKey()).between(pair.getFirst(), pair.getSecond()));
                case List<?> list -> criteriaDefinitions.add(where(entry.getKey()).in(list));
                case LikePattern likePattern -> criteriaDefinitions.add(where(entry.getKey()).like(likePattern));
                case Comparison comparison -> {
                    switch (comparison.operator()) {
                        case GREATER_THAN -> criteriaDefinitions.add(where(entry.getKey()).greaterThan(entry.getValue()));
                        case LESS_THAN -> criteriaDefinitions.add(where(entry.getKey()).lessThan(entry.getValue()));
                        case GREATER_THAN_OR_EQUAL -> criteriaDefinitions.add(where(entry.getKey()).greaterThanOrEquals(entry.getValue()));
                        case LESS_THAN_OR_EQUAL -> criteriaDefinitions.add(where(entry.getKey()).lessThanOrEquals(entry.getValue()));
                    }
                }
                case NullCheck nullCheck -> {
                    switch (nullCheck) {
                        case IS_NULL -> criteriaDefinitions.add(where(entry.getKey()).isNull());
                        case IS_NOT_NULL -> criteriaDefinitions.add(where(entry.getKey()).isNotNull());
                    }
                }
                case null, default -> criteriaDefinitions.add(where(entry.getKey()).is(entry.getValue()));
            }
        }
        return jdbcAggregateTemplate.findAll(Query.query(CriteriaDefinition.from(criteriaDefinitions)), tableName, pageable);
    }


    /**
     * Example usage of Map<String, Object> to define dynamic query conditions.
     *
     * Each entry in the map represents a condition on a database field.
     * The value’s type determines the type of condition to generate.
     *
     * 1. Equality Condition:
     *    conditions.put("fieldName", value);
     *    // SQL: fieldName = :fieldName
     *
     * 2. Range (BETWEEN) Condition:
     *    conditions.put("fieldName", Pair.of(start, end));
     *    // SQL: fieldName BETWEEN :fieldName_start AND :fieldName_end
     *
     * 3. IN Condition:
     *    conditions.put("fieldName", List.of(val1, val2, val3));
     *    // SQL: fieldName IN (:fieldName)
     *
     * 4. LIKE Condition:
     *    conditions.put("fieldName", new LikePattern("%" + value + "%"));
     *    // SQL: fieldName LIKE :fieldName
     *
     * 5. Comparison Operators (>, <, >=, <=):
     *    conditions.put("fieldName", new Comparison(value, Operator.GREATER_THAN));
     *    conditions.put("fieldName", new Comparison(value, Operator.LESS_THAN_OR_EQUAL));
     *    // SQL: fieldName > :fieldName OR fieldName <= :fieldName
     *
     * 6. Null Check:
     *    conditions.put("fieldName", NullCheck.IS_NULL);
     *    conditions.put("fieldName", NullCheck.IS_NOT_NULL);
     *    // SQL: fieldName IS NULL OR fieldName IS NOT NULL
     *
     * 7. Custom Expressions:
     *    conditions.put("fieldName", new RawExpression("fieldName LIKE CONCAT(:prefix, '%')"));
     *    // SQL: uses raw expression, injects bound params as needed
     *
     * Note:
     * - Ensure your query builder understands each value type and handles it accordingly.
     * - Use custom wrapper types like `LikePattern`, `Comparison`, or `NullCheck` enums for clarity.
     */
//    public <T> T findAll(String tableName, Map<String, Object> conditions, PageRequest pageable) {
//        StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " ");
//        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM " + tableName + " ");
//
//        if (!conditions.isEmpty()) {
//            sql.append("WHERE ");
//            countSql.append("WHERE ");
//            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
//                sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(" AND, ");
//                countSql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(" AND, ");
//            }
//            sql.deleteCharAt(sql.length() - 5);
//            countSql.deleteCharAt(sql.length() - 5);
//        }
//
//        Pair<Long, Long> pair = Pair.of(1L, 1L);
//        org.apache.commons.lang3.tuple.Pair<Long, Long> pair2 = org.apache.commons.lang3.tuple.Pair.of(1L, 1L);
//
//        List<CriteriaDefinition> criteriaDefinitions = new ArrayList<>();
//        criteriaDefinitions.add(where("name").is(""));
//        Query.query(CriteriaDefinition.from(criteriaDefinitions));
//        jdbcAggregateTemplate.findAll(Query.query(where().), pageable.withSort());
//
//        sql.append(" LIMIT :limit OFFSET :offset ");
//
//        long count = jdbcTemplate.queryForObject(countSql.toString(), conditions, Long.class);
//
//        conditions.put("offset", pageable.getPageNumber() * pageable.getPageSize());
//        conditions.put("limit", pageable.getPageSize());
//
//
//
//        return jdbcTemplate.query()
//    }
}
