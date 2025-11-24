package org.meristem.oneapp.notificationservice.repositories;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.notificationservice.dtos.sql.Comparison;
import org.meristem.oneapp.notificationservice.dtos.sql.LikePattern;
import org.meristem.oneapp.notificationservice.dtos.sql.NullCheck;
import org.meristem.oneapp.notificationservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.notificationservice.utils.AppUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.relational.core.query.Criteria.where;

@RequiredArgsConstructor
public class GeneralRepository {

    public final NamedParameterJdbcTemplate jdbcTemplate;
    public final JdbcAggregateTemplate jdbcAggregateTemplate;


    /**
     * <p>Updates records in the specified table based on the provided parameters and conditions.</p>
     * <p>Use the names as used in the db, not the object names, e.g. 'first_name' instead of 'firstName'</p>
     * <p>Accept the user input using a request object, then map to a map</p>
     *
     * @param <T>       The type of the entity corresponding to the table.
     * @param table     The class of the table to update, annotated with @Table.
     * @param updates    A map of column names and their new values to be updated.
     * @param condition A map of column names and their values to define the WHERE clause.
     *                  The keys represent column names, and the values represent the conditions.
     * @return The number of rows affected by the update operation.
     * @throws IllegalArgumentException if the table class is not annotated with @Table.
     */
    public <T> int dynamicUpdate(Class<T> table, Map<String, Object> updates, @NonNull Map<String, Object> condition) {

        if (condition.isEmpty()) {
            throw new BadRequestException("Condition cannot be empty");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + getTableName(table) + " SET ");

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(", ");
        }

        sql.append(" version = version + 1, last_modified_date = :last_modified_date, last_modified_by = :last_modified_by ");
        sql.append(" WHERE ");
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(" AND ");
        }
        sql.delete(sql.lastIndexOf("AND "), sql.length());
        updates.putAll(condition);
        updates.put("last_modified_date", LocalDateTime.now());
        updates.put("last_modified_by", AppUtil.getLoggedInSubject());

        return jdbcTemplate.update(sql.toString(), updates);
    }

    /**
     * <p>Delete records in the specified table based on the provided parameters and conditions.</p>
     * <p>Use the names as used in the db, not the object names, e.g. 'first_name' instead of 'firstName'</p>
     * <p>Accept the user input using a request object, then map to a map</p>
     *
     * @param <T>       The type of the entity corresponding to the table.
     * @param table     The class of the table to delete from, annotated with @Table.
     * @param conditions A map of column names and their values to define the WHERE clause.
     *                  The keys represent column names, and the values represent the conditions.
     * @return The number of rows affected by the delete operation.
     * @throws IllegalArgumentException if the table class is not annotated with @Table.
     */
    public <T> int dynamicDelete(Class<T> table, @NonNull Map<String, Object> conditions) {

        if (conditions.isEmpty()) {
            throw new BadRequestException("Condition cannot be empty");
        }

        StringBuilder sql = new StringBuilder("DELETE FROM " + getTableName(table) + " WHERE ");

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(" AND ");
        }

        sql.delete(sql.lastIndexOf("AND "), sql.length());

        return jdbcTemplate.update(sql.toString(), conditions);
    }



    /**
     * <p>Follow the instructions below</p>
     * <p>Example usage of Map<String, Object> to define dynamic query conditions.
     * Each entry in the map represents a condition on a database field.
     * The value’s type determines the type of condition to generate.</p>
     *
     * <p>1. Equality Condition:
     *    conditions.put("fieldName", value);
     *    // SQL: fieldName =: fieldName</p>
     *
     * <p>2. Range (BETWEEN) Condition:
     *    conditions.put("fieldName", Pair.of(start, end));
     *    // SQL: fieldName BETWEEN: fieldName_start AND: fieldName_end</p>
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
     *    conditions.put("fieldName", new Comparison (value, Operator.GREATER_THAN));
     *    conditions.put("fieldName", new Comparison (value, Operator.LESS_THAN_OR_EQUAL));
     *    // SQL: fieldName >: fieldName OR fieldName <=: fieldName</p>
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
     * <p>Follow the instructions below</p>
     * <p>Example usage of Map<String, Object> to define dynamic query conditions.
     * Each entry in the map represents a condition on a database field.
     * The value’s type determines the type of condition to generate.</p>
     *
     * <p>1. Equality Condition:
     *    conditions.put("fieldName", value);
     *    // SQL: fieldName =: fieldName</p>
     *
     * <p>2. Range (BETWEEN) Condition:
     *    conditions.put("fieldName", Pair.of(start, end));
     *    // SQL: fieldName BETWEEN: fieldName_start AND: fieldName_end</p>
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
     *    conditions.put("fieldName", new Comparison (value, Operator.GREATER_THAN));
     *    conditions.put("fieldName", new Comparison (value, Operator.LESS_THAN_OR_EQUAL));
     *    // SQL: fieldName >: fieldName OR fieldName <=: fieldName</p>
     *
     * <p>6. Null Check:
     *    conditions.put("fieldName", NullCheck.IS_NULL);
     *    conditions.put("fieldName", NullCheck.IS_NOT_NULL);
     *    // SQL: fieldName IS NULL OR fieldName IS NOT NULL</p>
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @param conditions A map of column names and their values to define the WHERE clause.
     *                   The keys represent column names, and the values represent the conditions.
     * @return A List containing the records that match the conditions.

     */
    public <T> List<T> findAllBy(Class<T> tableName, Map<String, Object> conditions) {

        return jdbcAggregateTemplate.findAll(Query.query(CriteriaDefinition.from(getCriteriaDefinitions(conditions))), tableName);
    }


    /**
     * <p>Follow the instructions below</p>
     *
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @return A List containing the records that match the conditions.

     */
    public <T> List<T> findAll(Class<T> tableName) {
        return jdbcAggregateTemplate.findAll(tableName);
    }


    /**
     * <p>Follow the instructions below</p>
     *
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @param conditions A map of column names and their values to define the WHERE clause.
     *                   The keys represent column names, and the values represent the conditions.
     * @return A List containing the records that match the conditions.

     */
    public <T, R> List<R> findAll(Class<T> tableName, Map<String, Object> conditions, RowMapper<R> rowMapper) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + getTableName(tableName) + " WHERE ");

        String query = getAllString(conditions, sql);

        return jdbcTemplate.query(query, conditions, rowMapper);
    }


    /**
     * <p>Follow the instructions below</p>
     *
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @return A List containing the records that match the conditions.

     */
    public <T, R> List<R> findAll(Class<T> tableName, RowMapper<R> rowMapper) {

        return jdbcTemplate.query("SELECT * FROM " + getTableName(tableName), rowMapper);
    }


    /**
     *
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @param conditions A map of column names and their values to define the WHERE clause.
     *                   The keys represent column names, and the values represent the conditions.
     * @param pageRequest The page request object containing the page number and page size.
     * @return A List containing the records that match the conditions.

     */
    public <T, R> Page<R> findAll(Class<T> tableName, Map<String, Object> conditions, RowMapper<R> rowMapper, PageRequest pageRequest) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + getTableName(tableName) + " WHERE ");

        String query = getAllString(conditions, sql);

        sql.append(" LIMIT :limit").append(" OFFSET :offset");
        conditions.put("limit", pageRequest.getPageSize());
        conditions.put("offset", pageRequest.getOffset() * pageRequest.getPageSize());

        List<R> page = jdbcTemplate.query(query, conditions, rowMapper);
        return new PageImpl<>(page, pageRequest, page.size());
    }

    /**
     *
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @param conditions A map of column names and their values to define the WHERE clause.
     *                   The keys represent column names, and the values represent the conditions.
     * @return An Optional containing the records that match the conditions.

     */
    public <T, R> Optional<R> findOneBy(Class<T> tableName, Map<String, Object> conditions, RowMapper<R> rowMapper) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + getTableName(tableName) + " WHERE ");

        String query = getAllString(conditions, sql);

        List<R> list = jdbcTemplate.query(query, conditions, rowMapper);
        return list.isEmpty() ? Optional.empty() : list.stream().findFirst();
    }


    /**
     *
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @param id The id of the item to search for.
     * @return An Optional containing the records that match the conditions.

     */
    public <T, R> Optional<R> findOneBy(Class<T> tableName, Long id, RowMapper<R> rowMapper) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        List<R> list = jdbcTemplate.query("SELECT * FROM " + getTableName(tableName) + " WHERE id = :id ", parameters, rowMapper);
        return list.isEmpty() ? Optional.empty() : list.stream().findFirst();
    }

    /**
     *
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @param conditions A map of column names and their values to define the WHERE clause.
     *                   The keys represent column names, and the values represent the conditions.
     * @return A List containing the records that match the conditions.

     */
    public <T, R> List<?> findAll(Class<T> tableName, Map<String, Object> conditions, ResultSetExtractor<List<?>> resultSetExtractor) {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + getTableName(tableName) + " WHERE ");

        String query = getAllString(conditions, sql);

        return jdbcTemplate.query(query, conditions, resultSetExtractor);
    }

    protected static String getAllString(Map<String, Object> conditions, StringBuilder sql) {
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            sql.append(entry.getKey()).append(" = :").append(entry.getKey()).append(" AND ");
        }

        return sql.toString().endsWith("WHERE ") ? sql.substring(0, sql.length() - 6) : sql.substring(0, sql.length() - 4);
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
     * <p>Follow the instructions below</p>
     * <p>Example usage of Map<String, Object> to define dynamic query conditions.
     * Each entry in the map represents a condition on a database field.
     * The value’s type determines the type of condition to generate.</p>
     *
     * <p>1. Equality Condition:
     *    conditions.put("fieldName", value);
     *    // SQL: fieldName =: fieldName</p>
     *
     * <p>2. Range (BETWEEN) Condition:
     *    conditions.put("fieldName", Pair.of(start, end));
     *    // SQL: fieldName BETWEEN: fieldName_start AND: fieldName_end</p>
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
     *    conditions.put("fieldName", new Comparison (value, Operator.GREATER_THAN));
     *    conditions.put("fieldName", new Comparison (value, Operator.LESS_THAN_OR_EQUAL));
     *    // SQL: fieldName >: fieldName OR fieldName <=: fieldName</p>
     *
     * <p>6. Null Check:
     *    conditions.put("fieldName", NullCheck.IS_NULL);
     *    conditions.put("fieldName", NullCheck.IS_NOT_NULL);
     *    // SQL: fieldName IS NULL OR fieldName IS NOT NULL</p>
     * @param <T>        The type of the entity corresponding to the table.
     * @param tableName  The class of the table to query, annotated with @Table.
     * @param conditions A map of column names and their values to define the WHERE clause.
     *                   The keys represent column names, and the values represent the conditions.
     * @return A Page containing the records that match the conditions.

     */
    public <T> boolean existBy(Class<T> tableName, Map<String, Object> conditions) {
        return jdbcAggregateTemplate.exists(Query.query(CriteriaDefinition.from(getCriteriaDefinitions(conditions))), tableName);
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
     *    // SQL: fieldName =: fieldName</p>
     *
     * <p>2. Range (BETWEEN) Condition:
     *    conditions.put("fieldName", Pair.of(start, end));
     *    // SQL: fieldName BETWEEN: fieldName_start AND: fieldName_end</p>
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
     *    conditions.put("fieldName", new Comparison (value, Operator.GREATER_THAN));
     *    conditions.put("fieldName", new Comparison (value, Operator.LESS_THAN_OR_EQUAL));
     *    // SQL: fieldName >: fieldName OR fieldName <=: fieldName</p>
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

    /**
     * Updates an existing record in the database.
     *
     * @param <T>      The type of the entity to update.
     * @param instance The instance of the entity to update. Must not be null.
     * @return The updated instance of the entity.
     */
    public <T> T update(T instance) {
        return jdbcAggregateTemplate.update(instance);
    }

    /**
     * Updates multiple existing records in the database.
     *
     * @param <T>       The type of the entities to update.
     * @param instances A list of entity instances to update. Must not be null or empty.
     * @return A list of updated entity instances.
     */
    public <T> List<T> update(List<T> instances) {
        return jdbcAggregateTemplate.updateAll(instances);
    }

    /**
     * Saves a new record in the database.
     *
     * @param <T>      The type of the entity to save.
     * @param instance The instance of the entity to save. Must not be null.
     * @return The saved instance of the entity.
     */
    public <T> T save(T instance) {
        return jdbcAggregateTemplate.save(instance);
    }

    /**
     * Saves multiple new records in the database.
     *
     * @param <T>       The type of the entities to save.
     * @param instances A list of entity instances to save. Must not be null or empty.
     * @return A list of saved entity instances.
     */
    public <T> List<T> saveAll(List<T> instances) {
        return jdbcAggregateTemplate.saveAll(instances);
    }


    /**
     * Deletes multiple records from the database.
     *
     * @param <T>       The type of the entities to delete.
     * @param instances A list of entity instances to delete. Must not be null or empty.
     */
    public <T> void deleteAll(List<T> instances) {
        jdbcAggregateTemplate.deleteAll(instances);
    }


    protected String getTableName(Class<?> table) {
        return StringUtils.isNotBlank(table.getAnnotation(Table.class).value()) ? table.getAnnotation(Table.class).value() : table.getAnnotation(Table.class).name();
    }

    public List<CriteriaDefinition> getCriteriaDefinitions(Map<String, Object> conditions) {
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
                        default -> throw new IllegalStateException("Unexpected value: " + comparison.operator());
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
}
