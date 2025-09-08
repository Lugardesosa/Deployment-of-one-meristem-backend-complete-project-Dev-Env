package org.meristem.oneapp.trustiesservice.repositories;


import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trustiesservice.domains.enums.Assets;
import org.meristem.oneapp.trustiesservice.domains.requests.UpdateSelectionRequest;
import org.meristem.oneapp.trustiesservice.domains.responses.GetAssetValueResponse;
import org.meristem.oneapp.trustiesservice.domains.responses.RealEstateResponse;
import org.meristem.oneapp.trustiesservice.dtos.sql.RowMappers;
import org.meristem.oneapp.trustiesservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.trustiesservice.models.*;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public List<?> findPlans(Class<?> clazz, Map<String, Object> filters, ResultSetExtractor<List<?>> resultSetExtractor, boolean withAssets) {

        String sql = "SELECT p.*, " +

                (withAssets ? "pa.asset_id AS assetId, pa.asset_type, " : "") +

                "pb.beneficiary_id, pb.percentage FROM " + getTableName(clazz) +
                " p " +

                (withAssets ? " LEFT JOIN plan_assets pa ON p.id = pa.plan_id " : "") +

                " LEFT JOIN plan_beneficiaries pb ON p.id = pb.plan_id " +
                " WHERE p.owner_id = :owner_id " +
                " AND pb.plan_type = :plan_type " +
                (filters.containsKey("id") ? " AND p.id = :id " : "");
        return jdbcTemplate.query(sql, filters, resultSetExtractor);
    }

    public Optional<RealEstateResponse.DocumentResponse> findFile(Class<?> clazz, Map<String, Object> fileFilter, RowMapper<RealEstateResponse.DocumentResponse> fileRowMapper) {

        String sql = getFileQuery(clazz, fileFilter);
        return jdbcTemplate.query(sql, fileFilter, fileRowMapper).stream().findFirst();
    }

    public List<RealEstateResponse.DocumentResponse> findFiles(Class<?> clazz, Map<String, Object> fileFilter, RowMapper<RealEstateResponse.DocumentResponse> fileRowMapper) {

        String sql = getFileQuery(clazz, fileFilter);
        return jdbcTemplate.query(sql, fileFilter, fileRowMapper);
    }

    private String getFileQuery(Class<?> clazz, Map<String, Object> fileFilter) {
        return "SELECT f.id, f.file_key, f.content_type, f.file_type FROM " + getTableName(clazz) +
                " f " +
                " LEFT JOIN entity_files ef ON ef.file_id = f.id " +
                (fileFilter.isEmpty() ? "" : " WHERE ") +
                (fileFilter.containsKey("entity_name") ? " ef.entity_name = :entity_name " : "") +
                (fileFilter.containsKey("entity_id") ? " AND ef.entity_id = :entity_id " : "") +
                (fileFilter.containsKey("owner_id") ? " AND f.owner_id = :owner_id " : "");
    }

    public List<GetAssetValueResponse.EstimatedValueDetails> getEstimatedValue(Long ownerId) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("SELECT currency_id, c.currency_logo, SUM(estimated_amount) AS es_value FROM ( ");

        for (Assets value : Assets.values()) {
            stringBuilder.append(" SELECT currency_id, estimated_amount FROM ").append(getTableName(value.getClazz())).append(" WHERE owner_id = :owner_id UNION ALL ");
        }

        stringBuilder.delete(stringBuilder.lastIndexOf("UNION ALL "), stringBuilder.length());

        stringBuilder.append(" ) t JOIN currencies c ON c.id = t.currency_id GROUP BY currency_id ");

        SqlParameterSource parameterSource = new MapSqlParameterSource("owner_id", ownerId);
        return jdbcTemplate.query(stringBuilder.toString(), parameterSource, RowMappers.getEstimatedAmount());
    }

    public List<GetAssetValueResponse.EstimatedValueDetails> getEstimatedValue(Assets assets, Long ownerId) {

        String stringBuilder = "SELECT t.currency_id, c.currency_logo, SUM(t.estimated_amount) AS es_value FROM " +
                getTableName(assets.getClazz()) +
                " t LEFT JOIN currencies c ON c.id = t.currency_id WHERE owner_id = :owner_id " +
                " GROUP BY t.currency_id ";

        SqlParameterSource parameterSource = new MapSqlParameterSource("owner_id", ownerId);
        return jdbcTemplate.query(stringBuilder, parameterSource, RowMappers.getEstimatedAmount());
    }

    public List<GetAssetValueResponse.EstimatedValueDetails.AssetEstimatedValueDetails> getCurrencyEstimatedValue(Long currencyId, Long loggedInUserId) {

        String sql = """
                    SELECT 'CASH' as table_name, COALESCE(SUM(estimated_amount), 0) AS total
                    FROM cash
                    WHERE currency_id = :currencyId AND owner_id = :ownerId
                
                    UNION ALL
                    SELECT 'PUBLIC_EQUITIES' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM public_equities
                    WHERE currency_id = :currencyId AND owner_id = :ownerId
                
                    UNION ALL
                    SELECT 'PRIVATE_EQUITIES' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM private_equities
                    WHERE currency_id = :currencyId AND owner_id = :ownerId
                
                    UNION ALL
                    SELECT 'REAL_ESTATE' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM real_estate
                    WHERE currency_id = :currencyId AND owner_id = :ownerId
                
                    UNION ALL
                    SELECT 'FIXED_INCOME_MONEY_MARKET' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM money_market
                    WHERE currency_id = :currencyId AND owner_id = :ownerId
                
                    UNION ALL
                    SELECT 'INTELLECTUAL_PROPERTY' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM intellectual_property
                    WHERE currency_id = :currencyId AND owner_id = :ownerId
                
                    UNION ALL
                    SELECT 'CRYPTO_NFT' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM alternate_assets
                    WHERE currency_id = :currencyId AND owner_id = :ownerId AND asset_type = 'Cryptocurrency & NFT'
                
                    UNION ALL
                    SELECT 'DIGITAL_PLATFORM' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM alternate_assets
                    WHERE currency_id = :currencyId AND owner_id = :ownerId AND asset_type = 'Digital Platform'
                
                    UNION ALL
                    SELECT 'FINTECH_WALLETS' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM alternate_assets
                    WHERE currency_id = :currencyId AND owner_id = :ownerId AND asset_type = 'Fintech Wallets'
                
                    UNION ALL
                    SELECT 'PERSONAL_ASSETS' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM personal_assets
                    WHERE currency_id = :currencyId AND owner_id = :ownerId
                
                    UNION ALL
                    SELECT 'PENSION' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM pension
                    WHERE currency_id = :currencyId AND owner_id = :ownerId
                
                    UNION ALL
                    SELECT 'LIFE_INSURANCE' as table_name, COALESCE(SUM(estimated_amount), 0)
                    FROM life_insurance
                    WHERE currency_id = :currencyId AND owner_id = :ownerId
                """;

        return jdbcTemplate.query(sql, Map.of("ownerId", loggedInUserId, "currencyId", currencyId), RowMappers.getCurrencyEstimatedAmount());

    }
}
