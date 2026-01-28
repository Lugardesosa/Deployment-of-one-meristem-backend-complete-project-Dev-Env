package org.meristem.oneapp.usersservice.config.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
@RequiredArgsConstructor
public class JdbcConfig {

    private final ObjectMapper mapper;

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Profile({"dev", "prod"})
    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        final List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add( new PostgresJsonWritingConverter(mapper));
        converters.add(new PostgresJsonReadingConverter(mapper));
        return new JdbcCustomConversions(converters);
    }

    @Profile({"local"})
    @Bean
    public JdbcCustomConversions jdbcCustomConversionsLocal() {
        final List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add( new H2JsonWritingConverter(mapper));
        converters.add(new H2JsonReadingConverter(mapper));
        return new JdbcCustomConversions(converters);
    }
}
