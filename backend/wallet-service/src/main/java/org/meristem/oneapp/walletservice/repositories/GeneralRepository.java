package org.meristem.oneapp.walletservice.repositories;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.walletservice.config.configProperties.WemaConfigProperties;
import org.meristem.oneapp.walletservice.domains.enums.NullCheck;
import org.meristem.oneapp.walletservice.dtos.sql.Comparison;
import org.meristem.oneapp.walletservice.dtos.sql.LikePattern;
import org.meristem.oneapp.walletservice.utils.AppUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.data.relational.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class GeneralRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JdbcAggregateTemplate jdbcAggregateTemplate;
    private final WemaConfigProperties wemaConfigProperties;


    /**
     * <p>Updates records in the specified table based on the provided parameters and conditions.</p>
     * <p>Use the names as used in the db, not the object names, eg 'first_name' instead of 'firstName'</p>
     * <p>Accept the user input using a request object, then map to a map</p>
     * @param <T>       The type of the entity corresponding to the table.
     * @param table     The class of the table to update, annotated with @Table.
     * @param params    A map of column names and their new values to be updated.
     * @param condition A map of column names and their values to define the WHERE clause.
     *                  The keys represent column names, and the values represent the conditions.
     * @return The number of rows affected by the update operation.
     * @throws IllegalArgumentException if the table class is not annotated with @Table.
     */
    public <T> int dynamicUpdate(Class<T> table, Map<String, Object> params, @NonNull Map<String, Object> condition) {

        StringBuilder sql = new StringBuilder("UPDATE " + getTableName(table) + " SET ");

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(", ");
        }

        sql.append(" version = version + 1, last_modified_date = :last_modified_date, last_modified_by = :last_modified_by ");
        sql.append(" WHERE ");
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(", ");
        }
        sql.deleteCharAt(sql.length() - 2);
        params.putAll(condition);
        params.put("last_modified_date", LocalDateTime.now());
        params.put("last_modified_by", AppUtil.getLoggedInSubject());

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
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @param conditions A map of column names and their values to define the WHERE clause.
     *                   The keys represent column names, and the values represent the conditions.
     * @param pageable   The pagination information, including page number and size.
     * @return A Page containing the records that match the conditions.

     */
    public <T> Page<T> findAllBy(Class<T> tableName, Map<String, Object> conditions, PageRequest pageable) {

        return jdbcAggregateTemplate.findAll(Query.query(CriteriaDefinition.from(getCriteriaDefinitions(conditions))), tableName, pageable);
    }

    /**
     * <p>Checks if a record with the specified ID exists in the given table.</p>
     *
     * @param <T>       The type of the entity corresponding to the table.
     * @param tableName The class of the table to check, annotated with @Table.
     * @param id        The ID of the record to check for existence.
     * @return True if a record with the specified ID exists, false otherwise.
     */
    public <T> boolean existById(Class<T> tableName, Long id) {
        return jdbcAggregateTemplate.exists(Query.query(where("id").is(id)), tableName);
    }

    /**
     * <p>Retrieves a record with the specified ID from the given table.</p>
     *
     * @param <T>       The type of the entity corresponding to the table.
     * @param tableName The class of the table to query, annotated with @Table.
     * @param id        The ID of the record to retrieve.
     * @return An Optional containing the record if found, or an empty Optional if not found.
     */
    public <T> Optional<T> findById(Class<T> tableName, Long id) {
        return jdbcAggregateTemplate.findOne(Query.query(where("id").is(id)), tableName);
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
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @param conditions A map of column names and their values to define the WHERE clause.
     *                   The keys represent column names, and the values represent the conditions.
     * @return An Optional containing the record if found, or an empty Optional if not found.

     */
    public <T> Optional<T> findOneBy(Class<T> tableName, Map<String, Object> conditions) {
        return jdbcAggregateTemplate.findOne(Query.query(CriteriaDefinition.from(getCriteriaDefinitions(conditions))), tableName);
    }


    private String getTableName(Class<?> table) {
        return StringUtils.isNotBlank(table.getAnnotation(Table.class).value()) ? table.getAnnotation(Table.class).value() : table.getAnnotation(Table.class).name();
    }

    private List<CriteriaDefinition> getCriteriaDefinitions(Map<String, Object> conditions) {
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
        return criteriaDefinitions;
    }

    public String generateWemaVirtualAccount() {
        Long nextVal = jdbcTemplate.queryForObject( "SELECT nextval('wema_virtual_account_seq')", Map.of(), Long.class);
        return wemaConfigProperties.accountPrefix() + String.format("%07d", nextVal);
    }
}
