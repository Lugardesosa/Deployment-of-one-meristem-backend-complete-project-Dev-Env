// TODO: TO REWORK AT A LATER TIME

//package org.meristem.oneapp.usersservice.repositories;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.core.namedparam.SqlParameterSource;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class TemplateRepository {
//
//    private final NamedParameterJdbcTemplate jdbcTemplate;
//
//    public boolean existsById(String tableName, Long id) {
//        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", 1);
//        jdbcTemplate.queryForObject("SELECT id FROM %s WHERE id = :id".formatted(tableName), new MapSqlParameterSource().addValue("id", 1), Long.class);
//    }
//}
